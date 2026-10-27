package com.acadhub.orche;

import java.io.Serializable;

public class NotificationItem implements Serializable {
    private final String id;
    private final String title;
    private final String message;
    private final String timeAgo;
    private boolean isRead;
    private final String type; // "ORDER", "STORE", "LOYALTY"

    public NotificationItem(String id, String title, String message, String timeAgo, boolean isRead, String type) {
        this.id = id;
        this.title = title;
        this.message = message;
        this.timeAgo = timeAgo;
        this.isRead = isRead;
        this.type = type;
    }

    public String getId() { return id; }
    public String getTitle() { return title; }
    public String getMessage() { return message; }
    public String getTimeAgo() { return timeAgo; }
    public boolean isRead() { return isRead; }
    public void setRead(boolean read) { isRead = read; }
    public String getType() { return type; }
}
