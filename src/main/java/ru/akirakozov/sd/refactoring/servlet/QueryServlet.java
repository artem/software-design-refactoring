package ru.akirakozov.sd.refactoring.servlet;

import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

/**
 * @author akirakozov
 */
public class QueryServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String command = request.getParameter("command");

        switch (command) {
            case "max":
                try {
                    try (Connection c = DriverManager.getConnection("jdbc:sqlite:test.db")) {
                        try (Statement stmt = c.createStatement()) {
                            try (ResultSet rs = stmt.executeQuery("SELECT * FROM PRODUCT ORDER BY PRICE DESC LIMIT 1")) {
                                response.getWriter().println("<html><body>");
                                response.getWriter().println("<h1>Product with max price: </h1>");

                                while (rs.next()) {
                                    String name = rs.getString("name");
                                    int price = rs.getInt("price");
                                    response.getWriter().println(name + "\t" + price + "</br>");
                                }
                                response.getWriter().println("</body></html>");
                            }
                        }
                    }
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
                break;
            case "min":
                try {
                    try (Connection c = DriverManager.getConnection("jdbc:sqlite:test.db")) {
                        try (Statement stmt = c.createStatement()) {
                            try (ResultSet rs = stmt.executeQuery("SELECT * FROM PRODUCT ORDER BY PRICE LIMIT 1")) {
                                response.getWriter().println("<html><body>");
                                response.getWriter().println("<h1>Product with min price: </h1>");

                                while (rs.next()) {
                                    String name = rs.getString("name");
                                    int price = rs.getInt("price");
                                    response.getWriter().println(name + "\t" + price + "</br>");
                                }
                                response.getWriter().println("</body></html>");
                            }
                        }
                    }

                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
                break;
            case "sum":
                try {
                    try (Connection c = DriverManager.getConnection("jdbc:sqlite:test.db")) {
                        try (Statement stmt = c.createStatement()) {
                            try (ResultSet rs = stmt.executeQuery("SELECT SUM(price) FROM PRODUCT")) {
                                response.getWriter().println("<html><body>");
                                response.getWriter().println("Summary price: ");

                                if (rs.next()) {
                                    response.getWriter().println(rs.getInt(1));
                                }
                                response.getWriter().println("</body></html>");
                            }
                        }
                    }

                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
                break;
            case "count":
                try {
                    try (Connection c = DriverManager.getConnection("jdbc:sqlite:test.db")) {
                        try (Statement stmt = c.createStatement()) {
                            try (ResultSet rs = stmt.executeQuery("SELECT COUNT(*) FROM PRODUCT")) {
                                response.getWriter().println("<html><body>");
                                response.getWriter().println("Number of products: ");

                                if (rs.next()) {
                                    response.getWriter().println(rs.getInt(1));
                                }
                                response.getWriter().println("</body></html>");
                            }
                        }
                    }

                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
                break;
            default:
                response.getWriter().println("Unknown command: " + command);
                break;
        }

        response.setContentType("text/html");
        response.setStatus(HttpServletResponse.SC_OK);
    }

}
