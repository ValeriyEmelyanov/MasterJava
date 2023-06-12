package ru.javaops.masterjava.matrix;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MainMatrix {
    private static final int MATRIX_SIZE = 1000;
    private static final int THREAD_NUMBER = Runtime.getRuntime().availableProcessors() > 2 ? Runtime.getRuntime().availableProcessors() - 2 : 1;

    private final static ExecutorService executor = Executors.newFixedThreadPool(MainMatrix.THREAD_NUMBER);

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        final int[][] matrixA = MatrixUtil.create(MATRIX_SIZE);
        final int[][] matrixB = MatrixUtil.create(MATRIX_SIZE);

        double singleTemplateThreadSum = 0.;
        double singleThreadSum = 0.;
        double singleThreadSum2 = 0.;
        double concurrentThreadSum = 0.;
        int count = 1;
        while (count < 6) {
            System.out.println("Pass " + count);

            long start = System.currentTimeMillis();
            final int[][] matrixTemplate = MatrixUtil.singleThreadMultiply(matrixA, matrixB);
            double duration = (System.currentTimeMillis() - start) / 1000.;
            out("Reference Single thread time, sec: %.3f", duration);
            singleTemplateThreadSum += duration;

            start = System.currentTimeMillis();
            final int[][] matrixC = MatrixUtil.singleThreadMultiplyQuick(matrixA, matrixB);
            duration = (System.currentTimeMillis() - start) / 1000.;
            out("Single thread time, sec: %.3f", duration);
            singleThreadSum += duration;

            if (!MatrixUtil.compare(matrixTemplate, matrixC)) {
                System.err.println("Comparison failed");
                break;
            }

            start = System.currentTimeMillis();
            final int[][] matrixC2 = MatrixUtil.singleThreadMultiplyQuick2(matrixA, matrixB);
            duration = (System.currentTimeMillis() - start) / 1000.;
            out("Single thread time (2), sec: %.3f", duration);
            singleThreadSum2 += duration;

            if (!MatrixUtil.compare(matrixTemplate, matrixC2)) {
                System.err.println("Comparison failed");
                break;
            }

            start = System.currentTimeMillis();
            final int[][] concurrentMatrixC = MatrixUtil.concurrentMultiply(matrixA, matrixB, executor);
            duration = (System.currentTimeMillis() - start) / 1000.;
            out("Concurrent thread time, sec: %.3f", duration);
            concurrentThreadSum += duration;

            if (!MatrixUtil.compare(matrixTemplate, concurrentMatrixC)) {
                System.err.println("Comparison failed");
                break;
            }

            count++;
        }
        executor.shutdown();
        out("\nAverage reference single thread time, sec: %.3f", singleTemplateThreadSum / 5.);
        out("Average single thread time, sec: %.3f", singleThreadSum / 5.);
        out("Average single thread time (2), sec: %.3f", singleThreadSum2 / 5.);
        out("Average concurrent thread time, sec: %.3f", concurrentThreadSum / 5.);
    }

    private static void out(String format, double ms) {
        System.out.println(String.format(format, ms));
    }
}
