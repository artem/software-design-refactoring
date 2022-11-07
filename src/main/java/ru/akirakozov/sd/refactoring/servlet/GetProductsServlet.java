package ru.akirakozov.sd.refactoring.servlet;

import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ru.akirakozov.sd.refactoring.dao.Database;
import ru.akirakozov.sd.refactoring.http.ResponseBuilder;

import java.io.IOException;

/**
 * @author akirakozov
 */
public class GetProductsServlet extends HttpServlet {

    private final Database db;

    public GetProductsServlet(Database db) {
        this.db = db;
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        final ResponseBuilder rb = new ResponseBuilder();
        try {
            rb.setProductResult(null, db.selectAll());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        response.getWriter().println(rb.build());
        response.setContentType("text/html");
        response.setStatus(HttpServletResponse.SC_OK);
    }
}
