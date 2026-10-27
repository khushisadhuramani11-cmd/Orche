package com.acadhub.orche;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.material.button.MaterialButton;

import java.util.List;

public class LoyaltyOffersFragment extends Fragment {

    private final AppState appState = AppState.getInstance();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.screen_loyalty_offers, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View root, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(root, savedInstanceState);

        MainActivity activity = (MainActivity) requireActivity();
        root.findViewById(R.id.backButton).setOnClickListener(v -> activity.onBackPressed());

        TextView txtBalance = root.findViewById(R.id.txtLoyaltyPointsBalance);
        txtBalance.setText(appState.getLoyaltyPoints() + " Fit Points");

        root.findViewById(R.id.btnRedeemPoints).setOnClickListener(v -> {
            appState.applyPromoCode("WELCOME");
            Toast.makeText(activity, "Redeemed 200 Fit Points (₹200 discount applied to bag)", Toast.LENGTH_SHORT).show();
            activity.navigateTo(new CartFragment(), true);
        });

        // Render Offers list
        LinearLayout container = root.findViewById(R.id.offersListContainer);
        container.removeAllViews();

        List<Offer> offers = appState.getOffers();
        for (Offer offer : offers) {
            LinearLayout card = new LinearLayout(activity);
            card.setOrientation(LinearLayout.VERTICAL);
            card.setPadding(36, 28, 36, 28);
            card.setBackgroundResource(R.drawable.card_background);

            // Title & Code
            LinearLayout topRow = new LinearLayout(activity);
            topRow.setOrientation(LinearLayout.HORIZONTAL);

            TextView title = new TextView(activity);
            title.setText(offer.getTitle());
            title.setTextColor(activity.getColor(R.color.orche_plum));
            title.setTextSize(14);
            title.setTypeface(null, android.graphics.Typeface.BOLD);
            LinearLayout.LayoutParams lpT = new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1);
            title.setLayoutParams(lpT);

            TextView code = new TextView(activity);
            code.setText(offer.getCode());
            code.setTextColor(activity.getColor(R.color.orche_charcoal));
            code.setTextSize(12);
            code.setTypeface(android.graphics.Typeface.MONOSPACE, android.graphics.Typeface.BOLD);
            code.setBackgroundResource(R.drawable.bg_pill_subtle);
            code.setPadding(20, 6, 20, 6);

            topRow.addView(title);
            topRow.addView(code);
            card.addView(topRow);

            // Description & Expiry
            TextView desc = new TextView(activity);
            desc.setText(offer.getDescription() + "\n" + offer.getExpiry());
            desc.setTextColor(activity.getColor(R.color.orche_muted));
            desc.setTextSize(11);
            desc.setPadding(0, 10, 0, 12);
            card.addView(desc);

            // Apply Button
            MaterialButton btnApply = new MaterialButton(activity);
            btnApply.setText("Apply Code " + offer.getCode());
            btnApply.setTextSize(11);
            btnApply.setAllCaps(false);
            btnApply.setBackgroundColor(activity.getColor(R.color.orche_rose_card));
            btnApply.setTextColor(activity.getColor(R.color.orche_plum));
            btnApply.setCornerRadius(16);
            btnApply.setOnClickListener(v -> {
                appState.applyPromoCode(offer.getCode());
                Toast.makeText(activity, "Coupon " + offer.getCode() + " applied to your bag!", Toast.LENGTH_SHORT).show();
                activity.navigateTo(new CartFragment(), true);
            });
            card.addView(btnApply);

            LinearLayout.LayoutParams lpCard = new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            lpCard.setMargins(0, 0, 0, 20);
            card.setLayoutParams(lpCard);

            container.addView(card);
        }
    }
}
