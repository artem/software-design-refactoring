package ru.akirakozov.sd.refactoring.dao;

public final class Product {
    private String name;
    private long price;

    public Product(final String name, final long price) {
        this.name = name;
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public long getPrice() {
        return price;
    }

    public void setPrice(final int price) {
        this.price = price;
    }
}