package ru.javaops.masterjava.webapp;

import com.google.common.collect.ImmutableMap;
import org.thymeleaf.context.WebContext;
import ru.javaops.masterjava.common.web.ThymeleafListener;
import ru.javaops.masterjava.persist.DBIProvider;
import ru.javaops.masterjava.persist.dao.UserDao;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("")
public class UserServlet extends HttpServlet {
    private final static int LIMIT = 20;
    private UserDao userDao = DBIProvider.getDao(UserDao.class);

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        final WebContext webContext = new WebContext(req, resp, req.getServletContext(), req.getLocale(),
                ImmutableMap.of("users", userDao.getWithLimit(LIMIT)));
        ThymeleafListener.engine.process("users", webContext, resp.getWriter());
    }
}
