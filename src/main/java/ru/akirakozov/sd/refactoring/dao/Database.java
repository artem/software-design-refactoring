package ru.akirakozov.sd.refactoring.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public class Database {

    private final String addr;

    public Database(final String addr) {
        this.addr = addr;
    }

    public void init() {
        executeUpdate("""
                CREATE TABLE IF NOT EXISTS PRODUCT
                (ID INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                 NAME TEXT NOT NULL,
                 PRICE INT NOT NULL);""");
    }

    public void drop() {
        executeUpdate("DROP TABLE PRODUCT;");
    }

    public List<Product> max() {
        return executeQuery("SELECT * FROM PRODUCT ORDER BY PRICE DESC LIMIT 1;", this::getProducts);
    }

    public List<Product> min() {
        return executeQuery("SELECT * FROM PRODUCT ORDER BY PRICE LIMIT 1;", this::getProducts);
    }

    public int count() {
        return executeQuery("SELECT COUNT(*) FROM PRODUCT;", this::getInt);
    }

    public int sum() {
        return executeQuery("SELECT SUM(price) FROM PRODUCT;", this::getInt);
    }

    public void insert(final Product product) {
        String query = "INSERT INTO PRODUCT " + "(NAME, PRICE) VALUES (\"" + product.getName() + "\"," + product.getPrice() + ");";
        executeUpdate(query);
    }

    public List<Product> selectAll() {
        return executeQuery("SELECT * FROM PRODUCT;", this::getProducts);
    }

    private int getInt(final ResultSet rs) {
        try {
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        throw new RuntimeException("malformed SQL response");
    }

    private List<Product> getProducts(final ResultSet rs) {
        List<Product> products = new ArrayList<>();
        try {
            while (rs.next()) {
                products.add(new Product(rs.getString("name"), rs.getInt("price")));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return products;
    }

    private void executeUpdate(final String query) {
        try {
            try (Connection c = DriverManager.getConnection(this.addr)) {
                try (Statement stmt = c.createStatement()) {
                    stmt.executeUpdate(query);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private <T> T executeQuery(final String query, final Function<ResultSet, ? extends T> mapper) {
        try {
            try (Connection c = DriverManager.getConnection(this.addr)) {
                try (Statement stmt = c.createStatement()) {
                    try (ResultSet rs = stmt.executeQuery(query)) {
                        return mapper.apply(rs);
                    }
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
