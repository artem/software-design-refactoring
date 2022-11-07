package ru.akirakozov.sd.refactoring.servlet;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.AssertionFailureBuilder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.stream.Stream;

import static org.mockito.Mockito.when;

public class ServletTest {

    final AddProductServlet addProductServlet = new AddProductServlet();
    final GetProductsServlet getProductsServlet = new GetProductsServlet();
    final QueryServlet queryServlet = new QueryServlet();

    @BeforeEach
    public void setUp() throws SQLException {
        try (final Connection c = DriverManager.getConnection("jdbc:sqlite:test.db")) {
            String sql = "CREATE TABLE IF NOT EXISTS PRODUCT" +
                         "(ID INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," +
                         " NAME           TEXT    NOT NULL, " +
                         " PRICE          INT     NOT NULL)";
            final Statement stmt = c.createStatement();

            stmt.executeUpdate(sql);
            stmt.close();

            final List<String> map = Stream.of("Seafood", "Meat", "Milk", "Eggs", "Molniya McQueen").sorted().toList();
            int price = 0;
            for (final String name : map) {
                String insertSql = "INSERT INTO PRODUCT " +
                                   "(NAME, PRICE) VALUES (\"" + name + "\"," + price + ")";
                Statement insertStmt = c.createStatement();
                insertStmt.executeUpdate(insertSql);
                insertStmt.close();
                price += 42;
            }
        }
    }

    @AfterEach
    public void tearDown() throws SQLException {
        try (final Connection c = DriverManager.getConnection("jdbc:sqlite:test.db")) {
            final Statement stmt = c.createStatement();
            stmt.executeUpdate("DROP TABLE PRODUCT");
            stmt.close();
        }
    }

    @Test
    public void testGetProducts() throws IOException {
        getProductsTester("""
                Eggs	0</br>
                Meat	42</br>
                Milk	84</br>
                Molniya McQueen	126</br>
                Seafood	168</br>
                """);
    }

    @Test
    public void testQueryMax() throws IOException {
        queryTester("max", """
                <h1>Product with max price: </h1>
                Seafood	168</br>
                """);
    }

    @Test
    public void testQueryMin() throws IOException {
        queryTester("min", """
                <h1>Product with min price: </h1>
                Eggs\t0</br>
                """);
    }

    @Test
    public void testQuerySum() throws IOException {
        queryTester("sum", """
                Summary price:\s
                420
                """);
    }

    @Test
    public void testQueryCount() throws IOException {
        queryTester("count", """
                Number of products:\s
                5
                """);
    }

    private void assertContains(final String expected, final String actual) {
        if (!actual.contains(expected)) {
            AssertionFailureBuilder.assertionFailure().expected(expected).actual(actual).buildAndThrow();
        }
    }

    void queryTester(String query, String responseString) throws IOException {
        final HttpServletResponse response = Mockito.mock(HttpServletResponse.class);
        final StringWriter stringWriter = new StringWriter();

        try (final PrintWriter printWriter = new PrintWriter(stringWriter)) {
            when(response.getWriter()).thenReturn(printWriter);
            final HttpServletRequest request = Mockito.mock(HttpServletRequest.class);
            when(request.getParameter("command")).thenReturn(query);
            queryServlet.doGet(request, response);
            assertContains(responseString, stringWriter.toString());
        }
    }

    private void getProductsTester(final String responseString) throws IOException {
        final HttpServletResponse response = Mockito.mock(HttpServletResponse.class);
        final StringWriter stringWriter = new StringWriter();

        try (final PrintWriter printWriter = new PrintWriter(stringWriter)) {
            when(response.getWriter()).thenReturn(printWriter);
            final HttpServletRequest request = Mockito.mock(HttpServletRequest.class);
            getProductsServlet.doGet(request, response);
            assertContains(responseString, stringWriter.toString());
        }
    }
}
