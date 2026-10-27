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

import java.util.List;

public class NotificationsFragment extends Fragment {

    private final AppState appState = AppState.getInstance();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.screen_notifications, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View root, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(root, savedInstanceState);

        MainActivity activity = (MainActivity) requireActivity();
        root.findViewById(R.id.backButton).setOnClickListener(v -> activity.onBackPressed());

        root.findViewById(R.id.btnMarkAllRead).setOnClickListener(v -> {
            appState.markAllNotificationsRead();
            renderNotifications(root);
            Toast.makeText(activity, "All notifications marked as read", Toast.LENGTH_SHORT).show();
        });

        renderNotifications(root);
    }

    private void renderNotifications(View root) {
        MainActivity activity = (MainActivity) requireActivity();
        LinearLayout container = root.findViewById(R.id.notificationsListContainer);
        container.removeAllViews();

        List<NotificationItem> list = appState.getNotifications();
        for (NotificationItem item : list) {
            LinearLayout itemLayout = new LinearLayout(activity);
            itemLayout.setOrientation(LinearLayout.VERTICAL);
            itemLayout.setPadding(36, 28, 36, 28);
            itemLayout.setBackgroundResource(item.isRead() ? R.drawable.card_background : R.drawable.bg_chip_selected);

            TextView title = new TextView(activity);
            title.setText(item.getTitle());
            title.setTextSize(13);
            title.setTextColor(activity.getColor(item.isRead() ? R.color.orche_charcoal : R.color.orche_white));
            title.setTypeface(null, android.graphics.Typeface.BOLD);

            TextView msg = new TextView(activity);
            msg.setText(item.getMessage());
            msg.setTextSize(12);
            msg.setTextColor(activity.getColor(item.isRead() ? R.color.orche_muted : R.color.orche_rose_card));
            msg.setPadding(0, 6, 0, 0);

            TextView time = new TextView(activity);
            time.setText(item.getTimeAgo());
            time.setTextSize(10);
            time.setTextColor(activity.getColor(item.isRead() ? R.color.orche_muted : R.color.orche_rose_accent));
            time.setPadding(0, 6, 0, 0);

            itemLayout.addView(title);
            itemLayout.addView(msg);
            itemLayout.addView(time);

            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            lp.setMargins(0, 0, 0, 18);
            itemLayout.setLayoutParams(lp);

            itemLayout.setOnClickListener(v -> {
                item.setRead(true);
                renderNotifications(root);
                if ("ORDER".equals(item.getType())) {
                    activity.navigateTo(new OrderHistoryFragment(), true);
                } else if ("STORE".equals(item.getType())) {
                    activity.showInStoreScannerDialog();
                } else if ("LOYALTY".equals(item.getType())) {
                    activity.navigateTo(new LoyaltyOffersFragment(), true);
                }
            });

            container.addView(itemLayout);
        }
    }
}
