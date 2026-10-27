package com.acadhub.orche;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class SupportFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.screen_support, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View root, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(root, savedInstanceState);

        MainActivity activity = (MainActivity) requireActivity();
        root.findViewById(R.id.backButton).setOnClickListener(v -> activity.onBackPressed());

        // Channel 1: Phone Intent (P0 Requirement - Intent Feature)
        root.findViewById(R.id.cardCallSupport).setOnClickListener(v -> {
            Intent dialIntent = new Intent(Intent.ACTION_DIAL);
            dialIntent.setData(Uri.parse("tel:+919820067243"));
            try {
                startActivity(dialIntent);
            } catch (Exception e) {
                Toast.makeText(activity, "Concierge Helpline: +91 98200 ORCHE", Toast.LENGTH_LONG).show();
            }
        });

        // Channel 2: Email Intent (P0 Requirement - Intent Feature)
        root.findViewById(R.id.cardEmailSupport).setOnClickListener(v -> {
            Intent emailIntent = new Intent(Intent.ACTION_SENDTO);
            emailIntent.setData(Uri.parse("mailto:concierge@orcheintimates.com"));
            emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Orche' Innerwear Fit & Order Support");
            try {
                startActivity(Intent.createChooser(emailIntent, "Send Email via"));
            } catch (Exception e) {
                Toast.makeText(activity, "Email: concierge@orcheintimates.com", Toast.LENGTH_LONG).show();
            }
        });

        // Channel 3: Google Maps Boutique Intent (Intent Feature)
        root.findViewById(R.id.cardMapBoutique).setOnClickListener(v -> {
            Uri gmmIntentUri = Uri.parse("geo:19.0596,72.8295?q=Linking+Road+Bandra+West+Mumbai");
            Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
            mapIntent.setPackage("com.google.android.apps.maps");
            try {
                startActivity(mapIntent);
            } catch (Exception e) {
                // Fallback to browser or any map app
                Intent webMapIntent = new Intent(Intent.ACTION_VIEW,
                        Uri.parse("https://maps.google.com/?q=Linking+Road+Bandra+West+Mumbai"));
                startActivity(webMapIntent);
            }
        });

        // Report Issue form
        EditText editOrderRef = root.findViewById(R.id.editOrderReference);
        EditText editIssue = root.findViewById(R.id.editIssueMessage);
        root.findViewById(R.id.btnSubmitIssue).setOnClickListener(v -> {
            String issueText = editIssue.getText().toString().trim();
            if (!issueText.isEmpty()) {
                Toast.makeText(activity, "Support ticket created. Our team will contact you within 2 hours.", Toast.LENGTH_LONG).show();
                editIssue.setText("");
                editOrderRef.setText("");
            } else {
                Toast.makeText(activity, "Please describe the problem before submitting.", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
