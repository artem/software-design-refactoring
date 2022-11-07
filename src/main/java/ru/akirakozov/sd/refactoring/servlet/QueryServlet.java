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
public class QueryServlet extends HttpServlet {

    private final Database db;

    public QueryServlet(Database db) {
        this.db = db;
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        final String command = request.getParameter("command");

        final ResponseBuilder rb = new ResponseBuilder();
        try {
            switch (command) {
                case "max" -> rb.setProductResult("Product with max price: ", db.max());
                case "min" -> rb.setProductResult("Product with min price: ", db.min());
                case "sum" -> rb.setNumberResult("Summary price: ", db.sum());
                case "count" -> rb.setNumberResult("Number of products: ", db.count());
                default -> rb.setArbitraryString("Unknown command: " + command);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        response.getWriter().println(rb.build());
        response.setContentType("text/html");
        response.setStatus(HttpServletResponse.SC_OK);
    }
}
