package com.acadhub.orche;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.List;

public class AddressManagementFragment extends Fragment {

    private final AppState appState = AppState.getInstance();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.screen_addresses, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View root, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(root, savedInstanceState);

        MainActivity activity = (MainActivity) requireActivity();
        root.findViewById(R.id.backButton).setOnClickListener(v -> activity.onBackPressed());

        // Location GPS Permission Trigger (Mobile App Dev Permission Feature)
        root.findViewById(R.id.btnDetectLocation).setOnClickListener(v -> activity.requestLocationPermissionAndDetect(this::onLocationDetected));

        // PIN Code Validator (P0 Requirement)
        EditText editPin = root.findViewById(R.id.editPinValidator);
        TextView txtResult = root.findViewById(R.id.txtPinResult);
        root.findViewById(R.id.btnCheckPin).setOnClickListener(v -> {
            String pin = editPin.getText().toString().trim();
            if (pin.length() == 6) {
                txtResult.setVisibility(View.VISIBLE);
                if (pin.startsWith("400")) {
                    txtResult.setText("✓ Fast Delivery (Same Day / 24h available) · Free In-Store Pickup at Bandra Boutique · COD Available");
                    txtResult.setTextColor(activity.getColor(R.color.orche_success));
                } else {
                    txtResult.setText("✓ Standard Delivery (2–4 business days) · 100% Discreet Packaging · Prepaid & COD Available");
                    txtResult.setTextColor(activity.getColor(R.color.orche_plum));
                }
            } else {
                Toast.makeText(activity, "Please enter a valid 6-digit PIN code", Toast.LENGTH_SHORT).show();
            }
        });

        // Add New Address
        root.findViewById(R.id.btnAddNewAddress).setOnClickListener(v -> showAddAddressDialog());

        renderAddresses(root);
    }

    private void renderAddresses(View root) {
        MainActivity activity = (MainActivity) requireActivity();
        LinearLayout container = root.findViewById(R.id.addressesContainer);
        container.removeAllViews();
        LayoutInflater inflater = getLayoutInflater();

        List<Address> list = appState.getAddresses();
        for (int i = 0; i < list.size(); i++) {
            Address addr = list.get(i);
            // Build address row
            LinearLayout itemLayout = new LinearLayout(activity);
            itemLayout.setOrientation(LinearLayout.VERTICAL);
            itemLayout.setPadding(36, 32, 36, 32);
            itemLayout.setBackgroundResource(addr.isDefault() ? R.drawable.bg_chip_selected : R.drawable.card_background);

            TextView title = new TextView(activity);
            title.setText(addr.getRecipientName() + " (" + addr.getLabel() + ")");
            title.setTextSize(14);
            title.setTextColor(activity.getColor(addr.isDefault() ? R.color.orche_white : R.color.orche_charcoal));
            title.setTypeface(null, android.graphics.Typeface.BOLD);

            TextView body = new TextView(activity);
            body.setText(addr.getFormattedAddress() + "\nPhone: " + addr.getPhone());
            body.setTextSize(12);
            body.setTextColor(activity.getColor(addr.isDefault() ? R.color.orche_rose_card : R.color.orche_muted));
            body.setPadding(0, 8, 0, 0);

            itemLayout.addView(title);
            itemLayout.addView(body);

            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            lp.setMargins(0, 0, 0, 20);
            itemLayout.setLayoutParams(lp);

            itemLayout.setOnClickListener(v -> {
                for (Address a : list) a.setDefault(false);
                addr.setDefault(true);
                appState.setSelectedAddress(addr);
                Toast.makeText(activity, "Selected " + addr.getLabel() + " for delivery", Toast.LENGTH_SHORT).show();
                renderAddresses(root);
            });

            container.addView(itemLayout);
        }
    }

    public void onLocationDetected(String city, String pinCode) {
        if (getView() != null) {
            EditText editPin = getView().findViewById(R.id.editPinValidator);
            if (editPin != null) {
                editPin.setText(pinCode);
                getView().findViewById(R.id.btnCheckPin).performClick();
            }
        }
    }

    private void showAddAddressDialog() {
        MainActivity activity = (MainActivity) requireActivity();
        LinearLayout layout = new LinearLayout(activity);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setPadding(40, 20, 40, 20);

        EditText editName = new EditText(activity);
        editName.setHint("Recipient Name");
        EditText editStreet = new EditText(activity);
        editStreet.setHint("Flat / House / Street Address");
        EditText editCity = new EditText(activity);
        editCity.setHint("City (e.g. Mumbai)");
        EditText editPin = new EditText(activity);
        editPin.setHint("PIN Code (e.g. 400050)");
        EditText editPhone = new EditText(activity);
        editPhone.setHint("Mobile Number");

        layout.addView(editName);
        layout.addView(editStreet);
        layout.addView(editCity);
        layout.addView(editPin);
        layout.addView(editPhone);

        new AlertDialog.Builder(activity)
                .setTitle("Add New Address")
                .setView(layout)
                .setPositiveButton("Save Address", (dialog, which) -> {
                    String name = editName.getText().toString().trim();
                    String street = editStreet.getText().toString().trim();
                    String city = editCity.getText().toString().trim();
                    String pin = editPin.getText().toString().trim();
                    String phone = editPhone.getText().toString().trim();

                    if (!name.isEmpty() && !street.isEmpty()) {
                        Address newAddr = new Address(
                                "addr_" + System.currentTimeMillis(),
                                "Other",
                                name,
                                street,
                                city.isEmpty() ? "Mumbai" : city,
                                "Maharashtra",
                                pin.isEmpty() ? "400050" : pin,
                                phone.isEmpty() ? "+91 98200 12345" : phone,
                                true
                        );
                        appState.addAddress(newAddr);
                        if (getView() != null) renderAddresses(getView());
                        Toast.makeText(activity, "Address saved successfully", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("Cancel", null)
                .show();
    }
}
