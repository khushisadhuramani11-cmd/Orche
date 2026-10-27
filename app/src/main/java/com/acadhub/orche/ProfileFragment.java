package com.acadhub.orche;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class ProfileFragment extends Fragment {

    private final AppState appState = AppState.getInstance();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.screen_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View root, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(root, savedInstanceState);

        MainActivity activity = (MainActivity) requireActivity();

        TextView txtName = root.findViewById(R.id.txtProfileName);
        TextView txtMembership = root.findViewById(R.id.txtProfileMembership);
        TextView btnAuth = root.findViewById(R.id.btnAuthAction);

        Runnable updateProfileUI = () -> {
            txtName.setText(appState.getUserName());
            if (appState.isLoggedIn()) {
                txtMembership.setText(appState.getMembershipTier() + " · " + appState.getLoyaltyPoints() + " Fit Points");
                btnAuth.setText("SWITCH");
            } else {
                txtMembership.setText("Guest Mode · Tap to save fitting data");
                btnAuth.setText("SIGN IN");
            }
        };

        btnAuth.setOnClickListener(v -> activity.showAuthDialog(updateProfileUI));

        // Notifications Button in Header
        root.findViewById(R.id.btnProfileNotifications).setOnClickListener(v -> activity.navigateTo(new NotificationsFragment(), true));

        // Menu Section 1: Orders & Addresses & Loyalty
        root.findViewById(R.id.menuOrderHistory).setOnClickListener(v -> activity.navigateTo(new OrderHistoryFragment(), true));
        root.findViewById(R.id.menuAddresses).setOnClickListener(v -> activity.navigateTo(new AddressManagementFragment(), true));
        root.findViewById(R.id.menuLoyaltyOffers).setOnClickListener(v -> activity.navigateTo(new LoyaltyOffersFragment(), true));

        // Menu Section 2: Fit, Hygiene & Help
        root.findViewById(R.id.menuSizeGuide).setOnClickListener(v -> activity.navigateTo(new SizeGuideFragment(), true));
        root.findViewById(R.id.menuHygienePolicy).setOnClickListener(v -> activity.navigateTo(new HygienePolicyFragment(), true));
        root.findViewById(R.id.menuSupport).setOnClickListener(v -> activity.navigateTo(new SupportFragment(), true));

        // Sign Out / Delete
        root.findViewById(R.id.btnSignOut).setOnClickListener(v -> {
            appState.logout();
            updateProfileUI.run();
            Toast.makeText(activity, "Signed out. Switched to Guest Mode", Toast.LENGTH_SHORT).show();
        });

        root.findViewById(R.id.btnDeleteAccount).setOnClickListener(v -> {
            new AlertDialog.Builder(activity)
                    .setTitle("Delete Account")
                    .setMessage("This will permanently remove your saved measurements, boutique fitting room history, and loyalty points. Are you sure?")
                    .setPositiveButton("Delete", (dialog, which) -> {
                        appState.logout();
                        updateProfileUI.run();
                        Toast.makeText(activity, "Account and fitting data deleted", Toast.LENGTH_SHORT).show();
                    })
                    .setNegativeButton("Cancel", null)
                    .show();
        });

        updateProfileUI.run();
    }
}
