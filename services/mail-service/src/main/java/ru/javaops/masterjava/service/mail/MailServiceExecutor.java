package ru.javaops.masterjava.service.mail;

import lombok.extern.slf4j.Slf4j;
import one.util.streamex.StreamEx;
import ru.javaops.masterjava.ExceptionType;
import ru.javaops.web.WebStateException;
import ru.javaops.web.WsClient;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.*;

@Slf4j
public class MailServiceExecutor {

    private static final String INTERRUPTED_BY_FAULTS_NUMBER = "+++ Interrupted by faults number";
    private static final String INTERRUPTED_BY_TIMEOUT = "+++ Interrupted by timeout";

    private static final ExecutorService mailExecutor = Executors.newFixedThreadPool(8);

    public static GroupResult sendBulk(final Set<Addressee> addressees, final String subject, final String body) throws WebStateException {
        final CompletionService<MailResult> completionService = new ExecutorCompletionService<>(mailExecutor);

        List<Future<MailResult>> futures = StreamEx.of(addressees)
                .map(addressee -> completionService.submit(() -> MailSender.sendTo(addressee, subject, body)))
                .toList();

        return new Callable<GroupResult>() {
            private int success = 0;
            private List<MailResult> failed = new ArrayList<>();

            @Override
            public GroupResult call() throws WebStateException {
                while (!futures.isEmpty()) {
                    try {
                        Future<MailResult> future = completionService.poll(10, TimeUnit.SECONDS);
                        if (future == null) {
                            cancel(INTERRUPTED_BY_TIMEOUT, null);
                        }
                        futures.remove(future);
                        MailResult mailResult = future.get();
                        if (mailResult.isOk()) {
                            success++;
                        } else {
                            failed.add(mailResult);
                            if (failed.size() >= 5) {
                                cancel(INTERRUPTED_BY_FAULTS_NUMBER, null);
                            }
                        }
                    } catch (ExecutionException e) {
                        cancel(null, e.getCause());
                    } catch (InterruptedException e) {
                        cancel(INTERRUPTED_BY_TIMEOUT, null);
                    }
                }
                GroupResult groupResult = new GroupResult(success, failed);
                log.info("groupResult: {}", groupResult);
                return groupResult;
            }

            private void cancel(String cause, Throwable t) throws WebStateException {
                futures.forEach(f -> f.cancel(true));
                if (cause != null) {
                    throw new WebStateException(cause, ExceptionType.EMAIL);
                } else {
                    throw WsClient.getWebStateException(t, ExceptionType.EMAIL);
                }
            }
        }.call();
    }
}