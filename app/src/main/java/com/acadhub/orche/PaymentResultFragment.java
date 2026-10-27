package com.acadhub.orche;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class PaymentResultFragment extends Fragment {

    private final AppState appState = AppState.getInstance();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.screen_payment_result, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View root, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(root, savedInstanceState);

        MainActivity activity = (MainActivity) requireActivity();

        TextView txtAmount = root.findViewById(R.id.txtPaymentAmount);
        txtAmount.setText("Amount Due: ₹" + appState.getTotalPayable());

        // Retry Payment Safely
        root.findViewById(R.id.btnRetryPayment).setOnClickListener(v -> {
            Toast.makeText(activity, "Re-initiating payment gateway...", Toast.LENGTH_SHORT).show();
            activity.onBackPressed();
        });

        // Choose Alternate Method
        root.findViewById(R.id.btnChooseAlternateMethod).setOnClickListener(v -> {
            activity.onBackPressed();
        });

        // Return to Shopping Bag
        root.findViewById(R.id.btnCancelReturnToBag).setOnClickListener(v -> {
            activity.navigateTo(new CartFragment(), false);
        });
    }
}
