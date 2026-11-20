package com.merida.tecnm.market_backend_v2.domain;

import com.fasterxml.jackson.annotation.JsonAlias;

public class Product {

    @JsonAlias({"idProducto", "id"})
    private Integer productId;

    @JsonAlias({"nombre"})
    private String name;

    @JsonAlias({"idCategoria"})
    private Integer categoryId;

    @JsonAlias({"precio", "precioVenta"})
    private double price;

    @JsonAlias({"cantidadStock"})
    private int stock;

    @JsonAlias({"estado"})
    private boolean active;

    private Category category;

    public Integer getProductId() {
        return productId;
    }

    public void setProductId(Integer productId) {
        this.productId = productId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Integer categoryId) {
        this.categoryId = categoryId;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }
}
