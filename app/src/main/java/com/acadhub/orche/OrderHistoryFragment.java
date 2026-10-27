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

public class OrderHistoryFragment extends Fragment {

    private final AppState appState = AppState.getInstance();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.screen_order_history, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View root, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(root, savedInstanceState);

        MainActivity activity = (MainActivity) requireActivity();
        root.findViewById(R.id.backButton).setOnClickListener(v -> activity.onBackPressed());

        LinearLayout container = root.findViewById(R.id.orderHistoryListContainer);
        container.removeAllViews();

        List<Order> orders = appState.getOrderHistory();
        for (Order order : orders) {
            LinearLayout card = new LinearLayout(activity);
            card.setOrientation(LinearLayout.VERTICAL);
            card.setPadding(36, 32, 36, 32);
            card.setBackgroundResource(R.drawable.card_background);

            // Order ID & Status Row
            LinearLayout header = new LinearLayout(activity);
            header.setOrientation(LinearLayout.HORIZONTAL);

            TextView id = new TextView(activity);
            id.setText(order.getId());
            id.setTextColor(activity.getColor(R.color.orche_charcoal));
            id.setTextSize(15);
            id.setTypeface(null, android.graphics.Typeface.BOLD);
            LinearLayout.LayoutParams lpId = new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1);
            id.setLayoutParams(lpId);

            TextView status = new TextView(activity);
            status.setText(order.getStatus());
            status.setTextColor(activity.getColor(order.getStatus().equals("Delivered") ? R.color.orche_success : R.color.orche_plum));
            status.setTextSize(12);
            status.setTypeface(null, android.graphics.Typeface.BOLD);

            header.addView(id);
            header.addView(status);
            card.addView(header);

            // Date & Estimate
            TextView meta = new TextView(activity);
            meta.setText("Ordered on " + order.getDate() + " · " + order.getDeliveryEstimate());
            meta.setTextColor(activity.getColor(R.color.orche_muted));
            meta.setTextSize(12);
            meta.setPadding(0, 8, 0, 0);
            card.addView(meta);

            // Items breakdown
            StringBuilder sb = new StringBuilder();
            for (CartItem ci : order.getItems()) {
                sb.append("• ").append(ci.getProduct().getName()).append(" (")
                        .append(ci.getSize()).append(", ").append(ci.getColor()).append(") × ")
                        .append(ci.getQuantity()).append("\n");
            }
            TextView items = new TextView(activity);
            items.setText(sb.toString().trim());
            items.setTextColor(activity.getColor(R.color.orche_charcoal));
            items.setTextSize(12);
            items.setPadding(0, 10, 0, 0);
            card.addView(items);

            // Total
            TextView total = new TextView(activity);
            total.setText("Total: ₹" + order.getTotalAmount());
            total.setTextColor(activity.getColor(R.color.orche_plum));
            total.setTextSize(14);
            total.setTypeface(null, android.graphics.Typeface.BOLD);
            total.setPadding(0, 10, 0, 14);
            card.addView(total);

            // Action Buttons Row (Track, Re-order, Return)
            LinearLayout actions = new LinearLayout(activity);
            actions.setOrientation(LinearLayout.HORIZONTAL);

            MaterialButton btnTrack = new MaterialButton(activity);
            btnTrack.setText("Track Order");
            btnTrack.setTextSize(11);
            btnTrack.setAllCaps(false);
            btnTrack.setBackgroundColor(activity.getColor(R.color.orche_plum));
            btnTrack.setTextColor(activity.getColor(R.color.orche_white));
            btnTrack.setCornerRadius(16);
            btnTrack.setOnClickListener(v -> activity.navigateTo(new TrackingFragment(), true));

            MaterialButton btnReorder = new MaterialButton(activity);
            btnReorder.setText("Re-order");
            btnReorder.setTextSize(11);
            btnReorder.setAllCaps(false);
            btnReorder.setBackgroundColor(activity.getColor(R.color.orche_rose_card));
            btnReorder.setTextColor(activity.getColor(R.color.orche_plum));
            btnReorder.setCornerRadius(16);
            LinearLayout.LayoutParams lpBtn = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            lpBtn.setMargins(16, 0, 0, 0);
            btnReorder.setLayoutParams(lpBtn);
            btnReorder.setOnClickListener(v -> {
                for (CartItem ci : order.getItems()) {
                    appState.addToCart(ci.getProduct(), ci.getColor(), ci.getSize());
                }
                activity.updateGlobalCartBadge();
                Toast.makeText(activity, "Items added to your bag", Toast.LENGTH_SHORT).show();
                activity.navigateTo(new CartFragment(), true);
            });

            actions.addView(btnTrack);
            actions.addView(btnReorder);
            card.addView(actions);

            LinearLayout.LayoutParams lpCard = new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            lpCard.setMargins(0, 0, 0, 24);
            card.setLayoutParams(lpCard);

            container.addView(card);
        }
    }
}
