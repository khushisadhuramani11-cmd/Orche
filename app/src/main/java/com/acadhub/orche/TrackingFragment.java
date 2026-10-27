package com.acadhub.orche;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class TrackingFragment extends Fragment {

    private final AppState appState = AppState.getInstance();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.screen_tracking, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View root, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(root, savedInstanceState);

        MainActivity activity = (MainActivity) requireActivity();
        root.findViewById(R.id.backButton).setOnClickListener(v -> activity.navigateTo(new HomeFragment(), false));

        root.findViewById(R.id.continueShoppingButton).setOnClickListener(v -> activity.navigateTo(new HomeFragment(), false));

        View cardStorePass = root.findViewById(R.id.cardStorePass);
        TextView txtEstimated = root.findViewById(R.id.txtEstimatedDelivery);
        TextView txtStepOut = root.findViewById(R.id.txtStepOutForDeliveryTitle);
        TextView txtOrderNumber = root.findViewById(R.id.txtOrderNumber);

        if (!appState.getOrderHistory().isEmpty()) {
            Order latest = appState.getOrderHistory().get(0);
            txtOrderNumber.setText(latest.getId());
        }

        if (appState.getFulfillmentMode() == AppState.FulfillmentMode.INSTORE_PICKUP) {
            cardStorePass.setVisibility(View.VISIBLE);
            txtEstimated.setText("Ready for pickup today at Bandra");
            txtStepOut.setText("Ready for pickup at counter");
        } else {
            cardStorePass.setVisibility(View.GONE);
            txtEstimated.setText("19–21 October");
            txtStepOut.setText("Out for delivery");
        }

        root.findViewById(R.id.btnGetHelp).setOnClickListener(v -> activity.navigateTo(new SupportFragment(), true));
    }
}
