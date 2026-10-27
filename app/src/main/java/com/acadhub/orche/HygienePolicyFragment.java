package com.acadhub.orche;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class HygienePolicyFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.screen_hygiene_policy, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View root, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(root, savedInstanceState);

        MainActivity activity = (MainActivity) requireActivity();
        root.findViewById(R.id.backButton).setOnClickListener(v -> activity.onBackPressed());

        root.findViewById(R.id.btnInitiateReturn).setOnClickListener(v -> showReturnInitiationDialog());
    }

    private void showReturnInitiationDialog() {
        MainActivity activity = (MainActivity) requireActivity();

        RadioGroup rg = new RadioGroup(activity);
        rg.setPadding(40, 20, 40, 20);

        RadioButton rb1 = new RadioButton(activity);
        rb1.setText("Soft Form Everyday Bra (Exchange for 34C)");
        rb1.setChecked(true);
        RadioButton rb2 = new RadioButton(activity);
        rb2.setText("Soft Form Everyday Bra (Refund to Original Payment)");
        RadioButton rb3 = new RadioButton(activity);
        rb3.setText("Cloud Silk Bralette (Size M to L Exchange)");

        rg.addView(rb1);
        rg.addView(rb2);
        rg.addView(rb3);

        new AlertDialog.Builder(activity)
                .setTitle("Select Garment to Return / Exchange")
                .setView(rg)
                .setPositiveButton("Submit Return Request", (dialog, which) -> {
                    new AlertDialog.Builder(activity)
                            .setTitle("Return Request Confirmed")
                            .setMessage("Reference: RET-ORCHE-7719\n\nOur discreet courier will pick up your unworn garment with tags intact within 48 hours.\n\nYour exchange item or refund will be processed immediately upon pickup.")
                            .setPositiveButton("Close", null)
                            .show();
                })
                .setNegativeButton("Cancel", null)
                .show();
    }
}
