package com.acadhub.orche;

import java.io.Serializable;

public class Offer implements Serializable {
    private final String code;
    private final String title;
    private final String description;
    private final String expiry;
    private final int minSpend;

    public Offer(String code, String title, String description, String expiry, int minSpend) {
        this.code = code;
        this.title = title;
        this.description = description;
        this.expiry = expiry;
        this.minSpend = minSpend;
    }

    public String getCode() { return code; }
    public String getTitle() { return title; }
    public String getDescription() { return description; }
    public String getExpiry() { return expiry; }
    public int getMinSpend() { return minSpend; }
}
