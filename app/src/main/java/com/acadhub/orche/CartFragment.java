package com.acadhub.orche;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.material.button.MaterialButton;

import java.util.List;

public class CartFragment extends Fragment {

    private final AppState appState = AppState.getInstance();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.screen_cart, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View root, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(root, savedInstanceState);

        MainActivity activity = (MainActivity) requireActivity();
        root.findViewById(R.id.backButton).setOnClickListener(v -> activity.onBackPressed());

        LinearLayout cartItemsContainer = root.findViewById(R.id.cartItemsContainer);
        TextView txtItemCountHeader = root.findViewById(R.id.txtCartItemCountHeader);
        TextView txtEmptyCart = root.findViewById(R.id.txtEmptyCart);
        TextView txtSubtotal = root.findViewById(R.id.txtCartSubtotal);
        TextView txtDiscount = root.findViewById(R.id.txtCartDiscount);
        View rowDiscount = root.findViewById(R.id.rowDiscount);
        TextView txtShipping = root.findViewById(R.id.txtCartShipping);
        TextView txtTotal = root.findViewById(R.id.txtCartTotal);
        MaterialButton checkoutBtn = root.findViewById(R.id.checkoutButton);

        TextView modeDelivery = root.findViewById(R.id.cartModeDelivery);
        TextView modeInStore = root.findViewById(R.id.cartModeInStore);

        EditText editPromo = root.findViewById(R.id.editPromoCode);
        MaterialButton btnApplyPromo = root.findViewById(R.id.btnApplyPromo);
        TextView txtPromoStatus = root.findViewById(R.id.txtPromoStatus);

        Runnable refreshCartUI = new Runnable() {
            @Override
            public void run() {
                cartItemsContainer.removeAllViews();
                List<CartItem> items = appState.getCartItems();
                int count = appState.getCartItemCount();

                txtItemCountHeader.setText(count + (count == 1 ? " ITEM" : " ITEMS"));

                if (items.isEmpty()) {
                    txtEmptyCart.setVisibility(View.VISIBLE);
                    checkoutBtn.setEnabled(false);
                } else {
                    txtEmptyCart.setVisibility(View.GONE);
                    checkoutBtn.setEnabled(true);

                    LayoutInflater inflater = getLayoutInflater();
                    for (int i = 0; i < items.size(); i++) {
                        final int itemIndex = i;
                        CartItem item = items.get(i);

                        View itemView = inflater.inflate(R.layout.item_cart_entry, cartItemsContainer, false);
                        ImageView img = itemView.findViewById(R.id.cartItemImage);
                        TextView title = itemView.findViewById(R.id.cartItemTitle);
                        TextView variant = itemView.findViewById(R.id.cartItemVariant);
                        TextView price = itemView.findViewById(R.id.cartItemPrice);
                        TextView qty = itemView.findViewById(R.id.txtQuantity);
                        TextView minus = itemView.findViewById(R.id.btnMinus);
                        TextView plus = itemView.findViewById(R.id.btnPlus);
                        TextView remove = itemView.findViewById(R.id.btnRemoveItem);

                        img.setImageResource(item.getProduct().getImageResId());
                        title.setText(item.getProduct().getName());
                        variant.setText(item.getColor() + " · " + item.getSize());
                        price.setText("₹" + item.getProduct().getPrice());
                        qty.setText(String.valueOf(item.getQuantity()));

                        minus.setOnClickListener(v -> {
                            appState.updateQuantity(itemIndex, -1);
                            activity.updateGlobalCartBadge();
                            run();
                        });

                        plus.setOnClickListener(v -> {
                            appState.updateQuantity(itemIndex, +1);
                            activity.updateGlobalCartBadge();
                            run();
                        });

                        remove.setOnClickListener(v -> {
                            appState.updateQuantity(itemIndex, -item.getQuantity());
                            activity.updateGlobalCartBadge();
                            run();
                        });

                        cartItemsContainer.addView(itemView);
                    }
                }

                // Subtotal
                int subtotal = appState.getSubtotal();
                txtSubtotal.setText("₹" + subtotal);

                // Discount
                int discount = appState.getDiscount();
                if (discount > 0) {
                    rowDiscount.setVisibility(View.VISIBLE);
                    txtDiscount.setText("-₹" + discount);
                } else {
                    rowDiscount.setVisibility(View.GONE);
                }

                // Shipping
                if (appState.getFulfillmentMode() == AppState.FulfillmentMode.INSTORE_PICKUP) {
                    txtShipping.setText("FREE (Store Pickup)");
                    txtShipping.setTextColor(activity.getColor(R.color.orche_success));
                } else {
                    txtShipping.setText("₹" + appState.getDeliveryFee());
                    txtShipping.setTextColor(activity.getColor(R.color.orche_charcoal));
                }

                // Total
                int total = appState.getTotalPayable();
                txtTotal.setText("₹" + total);
                checkoutBtn.setText("Proceed to checkout · ₹" + total);

                activity.updateGlobalCartBadge();
            }
        };

        // Mode Toggles in Cart
        modeDelivery.setOnClickListener(v -> {
            appState.setFulfillmentMode(AppState.FulfillmentMode.DELIVERY);
            modeDelivery.setBackgroundResource(R.drawable.bg_chip_selected);
            modeDelivery.setTextColor(activity.getColor(R.color.orche_white));
            modeInStore.setBackground(null);
            modeInStore.setTextColor(activity.getColor(R.color.orche_plum));
            refreshCartUI.run();
        });

        modeInStore.setOnClickListener(v -> {
            appState.setFulfillmentMode(AppState.FulfillmentMode.INSTORE_PICKUP);
            modeInStore.setBackgroundResource(R.drawable.bg_chip_selected);
            modeInStore.setTextColor(activity.getColor(R.color.orche_white));
            modeDelivery.setBackground(null);
            modeDelivery.setTextColor(activity.getColor(R.color.orche_plum));
            refreshCartUI.run();
        });

        // Initialize mode buttons
        if (appState.getFulfillmentMode() == AppState.FulfillmentMode.DELIVERY) {
            modeDelivery.setBackgroundResource(R.drawable.bg_chip_selected);
            modeDelivery.setTextColor(activity.getColor(R.color.orche_white));
            modeInStore.setBackground(null);
            modeInStore.setTextColor(activity.getColor(R.color.orche_plum));
        } else {
            modeInStore.setBackgroundResource(R.drawable.bg_chip_selected);
            modeInStore.setTextColor(activity.getColor(R.color.orche_white));
            modeDelivery.setBackground(null);
            modeDelivery.setTextColor(activity.getColor(R.color.orche_plum));
        }

        // Promo Code
        btnApplyPromo.setOnClickListener(v -> {
            String code = editPromo.getText().toString().trim();
            if (appState.applyPromoCode(code)) {
                txtPromoStatus.setVisibility(View.VISIBLE);
                txtPromoStatus.setText("Coupon " + code.toUpperCase() + " applied!");
                txtPromoStatus.setTextColor(activity.getColor(R.color.orche_success));
                refreshCartUI.run();
            } else {
                txtPromoStatus.setVisibility(View.VISIBLE);
                txtPromoStatus.setText("Invalid promo code. Try ORCHE10 for 10% off");
                txtPromoStatus.setTextColor(Color.RED);
            }
        });

        checkoutBtn.setOnClickListener(v -> {
            if (!appState.isLoggedIn() && appState.isGuest()) {
                // If customer is guest, prompt optional sign in or proceed seamlessly as guest
                activity.showAuthDialog(() -> activity.navigateTo(new CheckoutFragment(), true));
            } else {
                activity.navigateTo(new CheckoutFragment(), true);
            }
        });

        refreshCartUI.run();
    }
}
