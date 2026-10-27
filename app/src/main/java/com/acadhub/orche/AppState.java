package com.acadhub.orche;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class AppState {

    public enum FulfillmentMode {
        DELIVERY,
        INSTORE_PICKUP
    }

    private static AppState instance;

    // Catalog & Cart
    private final List<Product> catalog = new ArrayList<>();
    private final List<CartItem> cartItems = new ArrayList<>();
    private final List<String> wishlistProductIds = new ArrayList<>();

    // Addresses & Orders
    private final List<Address> addresses = new ArrayList<>();
    private Address selectedAddress;
    private final List<Order> orderHistory = new ArrayList<>();

    // Notifications & Offers
    private final List<NotificationItem> notifications = new ArrayList<>();
    private final List<Offer> offers = new ArrayList<>();
    private final List<Review> reviews = new ArrayList<>();

    // User State
    private boolean isLoggedIn = false;
    private boolean isGuest = true;
    private String userName = "Guest Customer";
    private String userEmail = "guest@orche.com";
    private String userPhone = "+91 98200 12345";
    private int loyaltyPoints = 450;
    private String membershipTier = "Orche' Circle Gold";

    // Preferences
    private boolean discreetPackaging = true;
    private boolean discreetBilling = true;
    private boolean notificationsEnabled = true;

    // Omnichannel & Cart State
    private FulfillmentMode fulfillmentMode = FulfillmentMode.DELIVERY;
    private String currentStore = "Bandra Boutique, Mumbai";
    private String appliedPromoCode = "";
    private int promoDiscount = 0;

    // Active product selection on detail screen
    private Product activeProduct;
    private String activeColor = "Rose Nude";
    private String activeSize = "34B";

    // In-Store / Fitting room state
    private String activeFittingRoom = "Fitting Room #3";
    private String lastFittingRoomRequest = null;

    // Filter & Sort State
    private String activeSort = "Recommended";
    private int filterMaxPrice = 3000;
    private final List<String> filterSizes = new ArrayList<>();

    private AppState() {
        initCatalog();
        initDefaultCart();
        initAddresses();
        initOrderHistory();
        initNotifications();
        initOffers();
        initReviews();
    }

    public static synchronized AppState getInstance() {
        if (instance == null) {
            instance = new AppState();
        }
        return instance;
    }

    private void initCatalog() {
        catalog.add(new Product(
                "p_bra_01",
                "Soft Form Everyday Bra",
                "EVERYDAY ESSENTIALS",
                "Bras",
                1199,
                4.8,
                "Fit-tested comfort",
                R.drawable.ic_bra_illustration,
                "Ultra-soft, wire-free microfiber support crafted for breathable all-day ease with zero pinch points.",
                new String[]{"30B", "32B", "34B", "36B", "38B"},
                new String[]{"Rose Nude", "Silk Ivory", "Deep Plum", "Mocha"},
                "Available at Bandra Boutique · Rack 4 (4 in stock)"
        ));

        catalog.add(new Product(
                "p_brief_01",
                "Everyday Seamless Brief",
                "EVERYDAY ESSENTIALS",
                "Briefs",
                499,
                4.9,
                "Cloud feel",
                R.drawable.ic_brief_illustration,
                "Laser-cut seamless edges that disappear under clothing. Made with natural breathable organic cotton lining.",
                new String[]{"XS", "S", "M", "L", "XL"},
                new String[]{"Rose Nude", "Silk Ivory", "Deep Plum", "Mocha"},
                "Available at Bandra Boutique · Rack 2 (8 in stock)"
        ));

        catalog.add(new Product(
                "p_bralette_02",
                "Cloud Silk Bralette",
                "LUXE COLLECTION",
                "Bras",
                1499,
                4.7,
                "Zero-wire freedom",
                R.drawable.ic_orche_o_monogram,
                "Featherweight modal-silk blend with delicate ribbed trim and subtle contour shaping.",
                new String[]{"32B", "34B", "36B", "38B"},
                new String[]{"Silk Ivory", "Rose Nude", "Deep Plum"},
                "Available at Bandra Boutique · Rack 5 (2 in stock)"
        ));

        catalog.add(new Product(
                "p_sleep_01",
                "Luxe Satin Sleep Set",
                "LOUNGE & SLEEP",
                "Sleepwear",
                2199,
                4.9,
                "Pure luxury",
                R.drawable.ic_orche_o_monogram,
                "Fluid draping satin cami and relaxed shorts designed for peaceful nights and slow mornings.",
                new String[]{"S", "M", "L", "XL"},
                new String[]{"Deep Plum", "Rose Nude"},
                "Available at Bandra Boutique · Rack 1 (5 in stock)"
        ));

        catalog.add(new Product(
                "p_set_01",
                "Everyday Essential Set",
                "CURATED DUO",
                "Sets",
                1599,
                4.8,
                "Best seller pair",
                R.drawable.ic_bra_illustration,
                "Matched Soft Form Bra & Seamless Brief in cohesive color palettes with bundle savings.",
                new String[]{"32B / S", "34B / M", "36B / L", "38B / XL"},
                new String[]{"Rose Nude", "Silk Ivory", "Deep Plum"},
                "Available at Bandra Boutique · Rack 3 (3 in stock)"
        ));

        activeProduct = catalog.get(0);
    }

    private void initDefaultCart() {
        cartItems.clear();
        cartItems.add(new CartItem(catalog.get(0), "Rose Nude", "34B", 1));
        cartItems.add(new CartItem(catalog.get(1), "Rose Nude", "M", 1));
    }

    private void initAddresses() {
        addresses.add(new Address(
                "addr_1",
                "Home",
                "Khwahish",
                "Flat 402, Sea Breeze, Linking Road, Bandra West",
                "Mumbai",
                "Maharashtra",
                "400050",
                "+91 98200 12345",
                true
        ));
        addresses.add(new Address(
                "addr_2",
                "Work",
                "Khwahish",
                "Tower 2, Floor 8, BKC Commercial Complex",
                "Mumbai",
                "Maharashtra",
                "400051",
                "+91 98200 12345",
                false
        ));
        selectedAddress = addresses.get(0);
    }

    private void initOrderHistory() {
        List<CartItem> pastItems1 = new ArrayList<>();
        pastItems1.add(new CartItem(catalog.get(0), "Rose Nude", "34B", 1));
        pastItems1.add(new CartItem(catalog.get(1), "Rose Nude", "M", 1));

        orderHistory.add(new Order(
                "OR-2026-10428",
                "28 Oct 2026",
                "Confirmed",
                new ArrayList<>(pastItems1),
                1758,
                false,
                "19–21 October",
                true
        ));

        List<CartItem> pastItems2 = new ArrayList<>();
        pastItems2.add(new CartItem(catalog.get(2), "Silk Ivory", "34B", 1));
        orderHistory.add(new Order(
                "OR-2026-09812",
                "12 Oct 2026",
                "Delivered",
                pastItems2,
                1499,
                false,
                "Delivered on 14 Oct",
                true
        ));

        List<CartItem> pastItems3 = new ArrayList<>();
        pastItems3.add(new CartItem(catalog.get(3), "Deep Plum", "M", 1));
        orderHistory.add(new Order(
                "OR-2026-08540",
                "28 Sep 2026",
                "Delivered (In-Store Pickup)",
                pastItems3,
                2199,
                true,
                "Picked up at Bandra Boutique",
                false
        ));
    }

    private void initNotifications() {
        notifications.add(new NotificationItem(
                "notif_1",
                "Order Dispatched · 100% Discreet Packaging",
                "Your order OR-2026-10428 is packed with no outer brand marks and on the way.",
                "10 mins ago",
                false,
                "ORDER"
        ));
        notifications.add(new NotificationItem(
                "notif_2",
                "Fitting Room Ready at Bandra Boutique",
                "Fitting Room #3 is reserved for your visit today. Your requested sizes are ready.",
                "2 hours ago",
                false,
                "STORE"
        ));
        notifications.add(new NotificationItem(
                "notif_3",
                "You earned 120 Fit Points!",
                "Your loyalty balance is now 450 points (worth ₹450 off future orders).",
                "1 day ago",
                true,
                "LOYALTY"
        ));
        notifications.add(new NotificationItem(
                "notif_4",
                "Restock Alert: Cloud Silk Bralette",
                "The wire-free Cloud Silk Bralette in Rose Nude 34B is back in stock.",
                "3 days ago",
                true,
                "PROMO"
        ));
    }

    private void initOffers() {
        offers.add(new Offer("ORCHE10", "10% Off Everything", "Get 10% discount across all innerwear & loungewear.", "Valid till 30 Nov", 0));
        offers.add(new Offer("WELCOME", "Flat ₹200 Off First Order", "Enjoy ₹200 instant savings on orders above ₹1,000.", "Valid till 31 Dec", 1000));
        offers.add(new Offer("FITTING50", "₹50 In-Store Trial Perk", "Complimentary saving after trying any fit in boutique.", "Valid till 15 Nov", 500));
        offers.add(new Offer("SILKSET", "₹300 Off Matched Sets", "Bundle savings on any bra and panty combination.", "Valid till 30 Nov", 1500));
    }

    private void initReviews() {
        reviews.add(new Review(
                "rev_1",
                "Ananya S. · Verified Buyer",
                5.0,
                "2 days ago",
                "True to size · Extremely comfortable",
                "Finally a wireless bra that actually supports without pinching or poking! The Rose Nude color is seamless under thin white shirts.",
                true,
                24
        ));
        reviews.add(new Review(
                "rev_2",
                "Pooja M. · Boutique Fitting Trial",
                4.8,
                "1 week ago",
                "True to size · Band is very soft",
                "Tried this at the Bandra boutique fitting room. The associate brought two sizes directly to my room. Super smooth fabric and feels like a second skin.",
                true,
                18
        ));
        reviews.add(new Review(
                "rev_3",
                "Rhea D. · Verified Buyer",
                5.0,
                "2 weeks ago",
                "True to size",
                "Packaging was 100% discreet as promised. Zero outer branding, delivered quickly, and the fit was spot on thanks to their size calculator.",
                true,
                12
        ));
    }

    // --- Authentication & User ---
    public boolean isLoggedIn() { return isLoggedIn; }
    public boolean isGuest() { return isGuest; }
    public String getUserName() { return userName; }
    public String getUserEmail() { return userEmail; }
    public String getUserPhone() { return userPhone; }
    public int getLoyaltyPoints() { return loyaltyPoints; }
    public String getMembershipTier() { return membershipTier; }

    public void login(String name, String email, String phone) {
        this.isLoggedIn = true;
        this.isGuest = false;
        if (name != null && !name.trim().isEmpty()) this.userName = name;
        if (email != null && !email.trim().isEmpty()) this.userEmail = email;
        if (phone != null && !phone.trim().isEmpty()) this.userPhone = phone;
    }

    public void continueAsGuest() {
        this.isLoggedIn = false;
        this.isGuest = true;
    }

    public void logout() {
        this.isLoggedIn = false;
        this.isGuest = true;
        this.userName = "Guest Customer";
    }

    // --- Preferences ---
    public boolean isDiscreetPackaging() { return discreetPackaging; }
    public void setDiscreetPackaging(boolean discreetPackaging) { this.discreetPackaging = discreetPackaging; }

    public boolean isDiscreetBilling() { return discreetBilling; }
    public void setDiscreetBilling(boolean discreetBilling) { this.discreetBilling = discreetBilling; }

    public boolean isNotificationsEnabled() { return notificationsEnabled; }
    public void setNotificationsEnabled(boolean notificationsEnabled) { this.notificationsEnabled = notificationsEnabled; }

    // --- Addresses ---
    public List<Address> getAddresses() { return addresses; }
    public Address getSelectedAddress() { return selectedAddress; }
    public void setSelectedAddress(Address address) { this.selectedAddress = address; }
    public void addAddress(Address address) {
        if (address.isDefault()) {
            for (Address a : addresses) a.setDefault(false);
            selectedAddress = address;
        }
        addresses.add(address);
    }

    // --- Orders ---
    public List<Order> getOrderHistory() { return orderHistory; }
    public void addOrder(Order order) {
        orderHistory.add(0, order);
        loyaltyPoints += (order.getTotalAmount() / 10);
    }

    // --- Notifications ---
    public List<NotificationItem> getNotifications() { return notifications; }
    public int getUnreadNotificationCount() {
        int count = 0;
        for (NotificationItem n : notifications) {
            if (!n.isRead()) count++;
        }
        return count;
    }
    public void markAllNotificationsRead() {
        for (NotificationItem n : notifications) n.setRead(true);
    }

    // --- Offers & Reviews ---
    public List<Offer> getOffers() { return offers; }
    public List<Review> getReviews() { return reviews; }
    public void addReview(Review review) { reviews.add(0, review); }

    // --- Catalog & Cart ---
    public List<Product> getCatalog() { return Collections.unmodifiableList(catalog); }

    public Product getProductById(String id) {
        for (Product p : catalog) {
            if (p.getId().equals(id)) return p;
        }
        return catalog.get(0);
    }

    public List<CartItem> getCartItems() { return cartItems; }

    public void addToCart(Product product, String color, String size) {
        for (CartItem item : cartItems) {
            if (item.getProduct().getId().equals(product.getId())
                    && item.getColor().equalsIgnoreCase(color)
                    && item.getSize().equalsIgnoreCase(size)) {
                item.setQuantity(item.getQuantity() + 1);
                return;
            }
        }
        cartItems.add(new CartItem(product, color, size, 1));
    }

    public void updateQuantity(int index, int delta) {
        if (index >= 0 && index < cartItems.size()) {
            CartItem item = cartItems.get(index);
            int newQty = item.getQuantity() + delta;
            if (newQty <= 0) {
                cartItems.remove(index);
            } else {
                item.setQuantity(newQty);
            }
        }
    }

    public void clearCart() {
        cartItems.clear();
        removePromoCode();
    }

    public int getCartItemCount() {
        int count = 0;
        for (CartItem item : cartItems) {
            count += item.getQuantity();
        }
        return count;
    }

    public int getSubtotal() {
        int total = 0;
        for (CartItem item : cartItems) {
            total += item.getLineTotal();
        }
        return total;
    }

    public int getDeliveryFee() {
        if (fulfillmentMode == FulfillmentMode.INSTORE_PICKUP) {
            return 0;
        }
        return getSubtotal() > 0 ? 60 : 0;
    }

    public boolean applyPromoCode(String code) {
        if (code == null) return false;
        String trimmed = code.trim().toUpperCase();
        if (trimmed.equals("ORCHE10")) {
            appliedPromoCode = "ORCHE10";
            promoDiscount = (int) (getSubtotal() * 0.10);
            return true;
        } else if (trimmed.equals("WELCOME")) {
            appliedPromoCode = "WELCOME";
            promoDiscount = Math.min(200, getSubtotal());
            return true;
        } else if (trimmed.equals("FITTING50")) {
            appliedPromoCode = "FITTING50";
            promoDiscount = Math.min(50, getSubtotal());
            return true;
        } else if (trimmed.equals("SILKSET")) {
            appliedPromoCode = "SILKSET";
            promoDiscount = Math.min(300, getSubtotal());
            return true;
        }
        return false;
    }

    public void removePromoCode() {
        appliedPromoCode = "";
        promoDiscount = 0;
    }

    public int getDiscount() {
        if (!appliedPromoCode.isEmpty()) {
            applyPromoCode(appliedPromoCode);
        }
        return promoDiscount;
    }

    public int getTotalPayable() {
        int subtotal = getSubtotal();
        if (subtotal == 0) return 0;
        int fee = getDeliveryFee();
        int discount = getDiscount();
        return Math.max(0, subtotal + fee - discount);
    }

    public FulfillmentMode getFulfillmentMode() { return fulfillmentMode; }
    public void setFulfillmentMode(FulfillmentMode mode) { this.fulfillmentMode = mode; }

    public String getCurrentStore() { return currentStore; }
    public void setCurrentStore(String store) { this.currentStore = store; }

    public String getAppliedPromoCode() { return appliedPromoCode; }

    public Product getActiveProduct() { return activeProduct; }
    public void setActiveProduct(Product product) {
        this.activeProduct = product;
        if (product != null && product.getSizes().length > 0) {
            this.activeSize = product.getSizes()[0];
        }
    }

    public String getActiveColor() { return activeColor; }
    public void setActiveColor(String color) { this.activeColor = color; }

    public String getActiveSize() { return activeSize; }
    public void setActiveSize(String size) { this.activeSize = size; }

    public boolean toggleWishlist(String productId) {
        if (wishlistProductIds.contains(productId)) {
            wishlistProductIds.remove(productId);
            return false;
        } else {
            wishlistProductIds.add(productId);
            return true;
        }
    }

    public boolean isWishlisted(String productId) {
        return wishlistProductIds.contains(productId);
    }

    public String getActiveFittingRoom() { return activeFittingRoom; }
    public void setActiveFittingRoom(String room) { this.activeFittingRoom = room; }

    public String getLastFittingRoomRequest() { return lastFittingRoomRequest; }
    public void setLastFittingRoomRequest(String item) { this.lastFittingRoomRequest = item; }

    // --- Filter & Sorting ---
    public String getActiveSort() { return activeSort; }
    public void setActiveSort(String activeSort) { this.activeSort = activeSort; }

    public int getFilterMaxPrice() { return filterMaxPrice; }
    public void setFilterMaxPrice(int filterMaxPrice) { this.filterMaxPrice = filterMaxPrice; }

    public List<String> getFilterSizes() { return filterSizes; }
    public void toggleFilterSize(String size) {
        if (filterSizes.contains(size)) filterSizes.remove(size);
        else filterSizes.add(size);
    }
    public void clearFilters() {
        filterSizes.clear();
        filterMaxPrice = 3000;
        activeSort = "Recommended";
    }
}
