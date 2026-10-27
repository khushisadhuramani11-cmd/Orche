package com.acadhub.orche;

import java.io.Serializable;

public class Product implements Serializable {
    private final String id;
    private final String name;
    private final String tag;
    private final String category;
    private final int price;
    private final double rating;
    private final String reviews;
    private final int imageResId;
    private final String description;
    private final String[] sizes;
    private final String[] colors;
    private final String inStoreStock;

    public Product(String id, String name, String tag, String category, int price,
                   double rating, String reviews, int imageResId, String description,
                   String[] sizes, String[] colors, String inStoreStock) {
        this.id = id;
        this.name = name;
        this.tag = tag;
        this.category = category;
        this.price = price;
        this.rating = rating;
        this.reviews = reviews;
        this.imageResId = imageResId;
        this.description = description;
        this.sizes = sizes;
        this.colors = colors;
        this.inStoreStock = inStoreStock;
    }

    public String getId() { return id; }
    public String getName() { return name; }
    public String getTag() { return tag; }
    public String getCategory() { return category; }
    public int getPrice() { return price; }
    public double getRating() { return rating; }
    public String getReviews() { return reviews; }
    public int getImageResId() { return imageResId; }
    public String getDescription() { return description; }
    public String[] getSizes() { return sizes; }
    public String[] getColors() { return colors; }
    public String getInStoreStock() { return inStoreStock; }
}
