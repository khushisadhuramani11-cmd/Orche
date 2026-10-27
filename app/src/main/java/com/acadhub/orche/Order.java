package com.acadhub.orche;

import java.io.Serializable;
import java.util.List;

public class Order implements Serializable {
    private final String id;
    private final String date;
    private final String status;
    private final List<CartItem> items;
    private final int totalAmount;
    private final boolean isStorePickup;
    private final String deliveryEstimate;
    private final boolean returnEligible;

    public Order(String id, String date, String status, List<CartItem> items,
                 int totalAmount, boolean isStorePickup, String deliveryEstimate, boolean returnEligible) {
        this.id = id;
        this.date = date;
        this.status = status;
        this.items = items;
        this.totalAmount = totalAmount;
        this.isStorePickup = isStorePickup;
        this.deliveryEstimate = deliveryEstimate;
        this.returnEligible = returnEligible;
    }

    public String getId() { return id; }
    public String getDate() { return date; }
    public String getStatus() { return status; }
    public List<CartItem> getItems() { return items; }
    public int getTotalAmount() { return totalAmount; }
    public boolean isStorePickup() { return isStorePickup; }
    public String getDeliveryEstimate() { return deliveryEstimate; }
    public boolean isReturnEligible() { return returnEligible; }
}
