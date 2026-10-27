package com.acadhub.orche;

import java.io.Serializable;

public class Review implements Serializable {
    private final String id;
    private final String authorName;
    private final double rating;
    private final String date;
    private final String fitFeedback; // "True to size", "Runs small", "Runs large"
    private final String comment;
    private final boolean verifiedPurchase;
    private int helpfulCount;

    public Review(String id, String authorName, double rating, String date,
                  String fitFeedback, String comment, boolean verifiedPurchase, int helpfulCount) {
        this.id = id;
        this.authorName = authorName;
        this.rating = rating;
        this.date = date;
        this.fitFeedback = fitFeedback;
        this.comment = comment;
        this.verifiedPurchase = verifiedPurchase;
        this.helpfulCount = helpfulCount;
    }

    public String getId() { return id; }
    public String getAuthorName() { return authorName; }
    public double getRating() { return rating; }
    public String getDate() { return date; }
    public String getFitFeedback() { return fitFeedback; }
    public String getComment() { return comment; }
    public boolean isVerifiedPurchase() { return verifiedPurchase; }
    public int getHelpfulCount() { return helpfulCount; }
    public void incrementHelpful() { this.helpfulCount++; }
}
