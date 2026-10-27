package com.acadhub.orche;

public class CartItem {
    private final Product product;
    private final String color;
    private final String size;
    private int quantity;

    public CartItem(Product product, String color, String size, int quantity) {
        this.product = product;
        this.color = color;
        this.size = size;
        this.quantity = quantity;
    }

    public Product getProduct() { return product; }
    public String getColor() { return color; }
    public String getSize() { return size; }
    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }

    public int getLineTotal() {
        return product.getPrice() * quantity;
    }
}
