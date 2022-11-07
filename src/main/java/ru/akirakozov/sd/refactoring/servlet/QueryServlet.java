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
public class QueryServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        final String command = request.getParameter("command");

        final ResponseBuilder rb = new ResponseBuilder();
        switch (command) {
            case "max":
                try {
                    List<Product> result = new ArrayList<>();
                    try (Connection c = DriverManager.getConnection("jdbc:sqlite:test.db")) {
                        try (Statement stmt = c.createStatement()) {
                            try (ResultSet rs = stmt.executeQuery("SELECT * FROM PRODUCT ORDER BY PRICE DESC LIMIT 1")) {
                                while (rs.next()) {
                                    result.add(new Product(rs.getString("name"), rs.getInt("price")));
                                }
                            }
                        }
                    }
                    rb.setProductResult("Product with max price: ", result);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
                break;
            case "min":
                try {
                    List<Product> result = new ArrayList<>();
                    try (Connection c = DriverManager.getConnection("jdbc:sqlite:test.db")) {
                        try (Statement stmt = c.createStatement()) {
                            try (ResultSet rs = stmt.executeQuery("SELECT * FROM PRODUCT ORDER BY PRICE LIMIT 1")) {
                                while (rs.next()) {
                                    result.add(new Product(rs.getString("name"), rs.getInt("price")));
                                }
                            }
                        }
                    }
                    rb.setProductResult("Product with min price: ", result);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
                break;
            case "sum":
                try {
                    int result = 0;
                    try (Connection c = DriverManager.getConnection("jdbc:sqlite:test.db")) {
                        try (Statement stmt = c.createStatement()) {
                            try (ResultSet rs = stmt.executeQuery("SELECT SUM(price) FROM PRODUCT")) {
                                if (rs.next()) {
                                    result = rs.getInt(1);
                                }
                            }
                        }
                    }
                    rb.setNumberResult("Summary price: ", result);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
                break;
            case "count":
                try {
                    int result = 0;
                    try (Connection c = DriverManager.getConnection("jdbc:sqlite:test.db")) {
                        try (Statement stmt = c.createStatement()) {
                            try (ResultSet rs = stmt.executeQuery("SELECT COUNT(*) FROM PRODUCT")) {
                                if (rs.next()) {
                                    result = rs.getInt(1);
                                }
                            }
                        }
                    }
                    rb.setNumberResult("Number of products: ", result);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
                break;
            default:
                rb.setArbitraryString("Unknown command: " + command);
                break;
        }

        response.getWriter().println(rb.build());
        response.setContentType("text/html");
        response.setStatus(HttpServletResponse.SC_OK);
    }

}
