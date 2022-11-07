package ru.akirakozov.sd.refactoring.servlet;

import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ru.akirakozov.sd.refactoring.dao.Product;
import ru.akirakozov.sd.refactoring.http.ResponseBuilder;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * @author akirakozov
 */
public class GetProductsServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        final ResponseBuilder rb = new ResponseBuilder();
        try {
            List<Product> result = new ArrayList<>();
            try (Connection c = DriverManager.getConnection("jdbc:sqlite:test.db")) {
                try (Statement stmt = c.createStatement()) {
                    try (ResultSet rs = stmt.executeQuery("SELECT * FROM PRODUCT")) {
                        while (rs.next()) {
                             result.add(new Product(rs.getString("name"), rs.getInt("price")));
                        }
                    }
                }
            }
            rb.setProductResult(null, result);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        response.getWriter().println(rb.build());
        response.setContentType("text/html");
        response.setStatus(HttpServletResponse.SC_OK);
    }
}
