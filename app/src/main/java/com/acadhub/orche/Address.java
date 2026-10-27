package com.acadhub.orche;

import java.io.Serializable;

public class Address implements Serializable {
    private final String id;
    private final String label;
    private final String recipientName;
    private final String street;
    private final String city;
    private final String state;
    private final String pinCode;
    private final String phone;
    private boolean isDefault;

    public Address(String id, String label, String recipientName, String street,
                   String city, String state, String pinCode, String phone, boolean isDefault) {
        this.id = id;
        this.label = label;
        this.recipientName = recipientName;
        this.street = street;
        this.city = city;
        this.state = state;
        this.pinCode = pinCode;
        this.phone = phone;
        this.isDefault = isDefault;
    }

    public String getId() { return id; }
    public String getLabel() { return label; }
    public String getRecipientName() { return recipientName; }
    public String getStreet() { return street; }
    public String getCity() { return city; }
    public String getState() { return state; }
    public String getPinCode() { return pinCode; }
    public String getPhone() { return phone; }
    public boolean isDefault() { return isDefault; }
    public void setDefault(boolean aDefault) { isDefault = aDefault; }

    public String getFormattedAddress() {
        return street + "\n" + city + ", " + state + " " + pinCode;
    }
}
