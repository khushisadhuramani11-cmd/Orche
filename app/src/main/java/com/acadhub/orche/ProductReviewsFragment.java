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

public class ProductReviewsFragment extends Fragment {

    private final AppState appState = AppState.getInstance();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.screen_product_reviews, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View root, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(root, savedInstanceState);

        MainActivity activity = (MainActivity) requireActivity();
        root.findViewById(R.id.backButton).setOnClickListener(v -> activity.onBackPressed());

        root.findViewById(R.id.btnWriteReview).setOnClickListener(v -> activity.showWriteReviewDialog(this::renderReviews));

        renderReviews();
    }

    private void renderReviews() {
        if (getView() == null) return;
        MainActivity activity = (MainActivity) requireActivity();
        LinearLayout container = getView().findViewById(R.id.reviewsListContainer);
        container.removeAllViews();

        List<Review> reviews = appState.getReviews();
        for (Review rev : reviews) {
            LinearLayout card = new LinearLayout(activity);
            card.setOrientation(LinearLayout.VERTICAL);
            card.setPadding(36, 28, 36, 28);
            card.setBackgroundResource(R.drawable.card_background);

            // Author & Date
            LinearLayout rowHeader = new LinearLayout(activity);
            rowHeader.setOrientation(LinearLayout.HORIZONTAL);

            TextView author = new TextView(activity);
            author.setText(rev.getAuthorName());
            author.setTextColor(activity.getColor(R.color.orche_charcoal));
            author.setTextSize(13);
            author.setTypeface(null, android.graphics.Typeface.BOLD);
            LinearLayout.LayoutParams lpA = new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1);
            author.setLayoutParams(lpA);

            TextView date = new TextView(activity);
            date.setText(rev.getDate());
            date.setTextColor(activity.getColor(R.color.orche_muted));
            date.setTextSize(11);

            rowHeader.addView(author);
            rowHeader.addView(date);
            card.addView(rowHeader);

            // Rating Stars & Fit tag
            TextView ratingAndFit = new TextView(activity);
            ratingAndFit.setText("★★★★★  ·  " + rev.getFitFeedback());
            ratingAndFit.setTextColor(activity.getColor(R.color.orche_plum));
            ratingAndFit.setTextSize(12);
            ratingAndFit.setTypeface(null, android.graphics.Typeface.BOLD);
            ratingAndFit.setPadding(0, 6, 0, 0);
            card.addView(ratingAndFit);

            // Comment
            TextView comment = new TextView(activity);
            comment.setText(rev.getComment());
            comment.setTextColor(activity.getColor(R.color.orche_charcoal));
            comment.setTextSize(12);
            comment.setLineSpacing(4, 1);
            comment.setPadding(0, 8, 0, 0);
            card.addView(comment);

            // Helpful footer
            TextView helpful = new TextView(activity);
            helpful.setText("Helpful (" + rev.getHelpfulCount() + ") 👍");
            helpful.setTextColor(activity.getColor(R.color.orche_muted));
            helpful.setTextSize(11);
            helpful.setPadding(0, 10, 0, 0);
            helpful.setOnClickListener(v -> {
                rev.incrementHelpful();
                helpful.setText("Helpful (" + rev.getHelpfulCount() + ") 👍");
                Toast.makeText(activity, "Marked as helpful", Toast.LENGTH_SHORT).show();
            });
            card.addView(helpful);

            LinearLayout.LayoutParams lpCard = new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            lpCard.setMargins(0, 0, 0, 18);
            card.setLayoutParams(lpCard);

            container.addView(card);
        }
    }
}
