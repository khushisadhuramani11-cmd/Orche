package com.acadhub.orche;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.material.button.MaterialButton;

import java.util.ArrayList;

public class CheckoutFragment extends Fragment {

    private final AppState appState = AppState.getInstance();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.screen_checkout, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View root, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(root, savedInstanceState);

        MainActivity activity = (MainActivity) requireActivity();
        root.findViewById(R.id.backButton).setOnClickListener(v -> activity.onBackPressed());

        TextView txtDestHeader = root.findViewById(R.id.txtDestinationHeader);
        TextView txtAddressBody = root.findViewById(R.id.txtAddressBody);
        TextView txtCustomerName = root.findViewById(R.id.txtCustomerName);
        RadioButton radioStandard = root.findViewById(R.id.radioStandardDelivery);
        RadioButton radioPickup = root.findViewById(R.id.radioStorePickup);
        View cardStandard = root.findViewById(R.id.cardMethodStandard);
        View cardPickup = root.findViewById(R.id.cardMethodPickup);
        TextView txtTotalPayable = root.findViewById(R.id.txtCheckoutTotalPayable);
        MaterialButton btnPlaceOrder = root.findViewById(R.id.placeOrderButton);
        RadioGroup radioGroupPayment = root.findViewById(R.id.radioGroupPayment);

        // Address change button -> opens AddressManagementFragment
        root.findViewById(R.id.btnChangeAddress).setOnClickListener(v -> activity.navigateTo(new AddressManagementFragment(), true));

        Runnable refreshCheckout = () -> {
            boolean isPickup = appState.getFulfillmentMode() == AppState.FulfillmentMode.INSTORE_PICKUP;
            if (isPickup) {
                txtDestHeader.setText("Pickup Boutique");
                txtCustomerName.setText(appState.getUserName());
                txtAddressBody.setText("Orche' Boutique Bandra Flagship\nLinking Road, Bandra West, Mumbai 400050\nReady in 1 hour");
                radioPickup.setChecked(true);
                radioStandard.setChecked(false);
                cardPickup.setBackgroundResource(R.drawable.bg_chip_selected);
                cardStandard.setBackgroundResource(R.drawable.card_background);
            } else {
                txtDestHeader.setText("Delivery address");
                Address addr = appState.getSelectedAddress();
                if (addr != null) {
                    txtCustomerName.setText(addr.getRecipientName() + " (" + addr.getLabel() + ")");
                    txtAddressBody.setText(addr.getFormattedAddress());
                } else {
                    txtCustomerName.setText(appState.getUserName());
                    txtAddressBody.setText("Home · Linking Road, Bandra West\nMumbai, Maharashtra 400050");
                }
                radioStandard.setChecked(true);
                radioPickup.setChecked(false);
                cardStandard.setBackgroundResource(R.drawable.bg_chip_selected);
                cardPickup.setBackgroundResource(R.drawable.card_background);
            }

            int total = appState.getTotalPayable();
            txtTotalPayable.setText("₹" + total);
            btnPlaceOrder.setText("Place order · ₹" + total);
        };

        cardStandard.setOnClickListener(v -> {
            appState.setFulfillmentMode(AppState.FulfillmentMode.DELIVERY);
            refreshCheckout.run();
        });

        cardPickup.setOnClickListener(v -> {
            appState.setFulfillmentMode(AppState.FulfillmentMode.INSTORE_PICKUP);
            refreshCheckout.run();
        });

        refreshCheckout.run();

        // Place Order with Payment Selection & Simulation Dialog (P0 Requirement)
        btnPlaceOrder.setOnClickListener(v -> {
            int selectedPaymentId = radioGroupPayment.getCheckedRadioButtonId();
            if (selectedPaymentId == R.id.payCounter) {
                // Immediate COD / In-store payment success
                completeOrder(activity);
            } else {
                // Simulate payment gateway with options to test Success OR Failure/Retry!
                new AlertDialog.Builder(activity)
                        .setTitle("Simulate Payment Gateway")
                        .setMessage("Total Payable: ₹" + appState.getTotalPayable() + "\nMethod: Online Gateway / UPI\n\nChoose an outcome to test both checkout flows:")
                        .setPositiveButton("Simulate Success", (dialog, which) -> completeOrder(activity))
                        .setNegativeButton("Simulate Failure (Test Retry)", (dialog, which) -> {
                            activity.navigateTo(new PaymentResultFragment(), true);
                        })
                        .show();
            }
        });
    }

    private void completeOrder(MainActivity activity) {
        Order newOrder = new Order(
                "OR-2026-" + ((int)(Math.random() * 90000) + 10000),
                "Today",
                "Confirmed",
                new ArrayList<>(appState.getCartItems()),
                appState.getTotalPayable(),
                appState.getFulfillmentMode() == AppState.FulfillmentMode.INSTORE_PICKUP,
                appState.getFulfillmentMode() == AppState.FulfillmentMode.INSTORE_PICKUP ? "Ready in 1 hour" : "19–21 October",
                true
        );
        appState.addOrder(newOrder);
        appState.clearCart();
        activity.updateGlobalCartBadge();
        activity.navigateTo(new TrackingFragment(), false);
    }
}
