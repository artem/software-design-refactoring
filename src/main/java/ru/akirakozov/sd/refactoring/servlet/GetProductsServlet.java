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
public class GetProductsServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        try {
            try (Connection c = DriverManager.getConnection("jdbc:sqlite:test.db")) {
                try (Statement stmt = c.createStatement()) {
                    try (ResultSet rs = stmt.executeQuery("SELECT * FROM PRODUCT")) {
                        response.getWriter().println("<html><body>");

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

        response.setContentType("text/html");
        response.setStatus(HttpServletResponse.SC_OK);
    }
}
