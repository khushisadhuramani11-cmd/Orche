package com.acadhub.orche;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.material.button.MaterialButton;

public class SavedFragment extends Fragment {

    private final AppState appState = AppState.getInstance();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.screen_saved, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View root, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(root, savedInstanceState);

        MainActivity activity = (MainActivity) requireActivity();

        LinearLayout container = root.findViewById(R.id.savedItemsContainer);
        View emptyLayout = root.findViewById(R.id.layoutEmptyWishlist);
        TextView countText = root.findViewById(R.id.txtSavedCount);

        container.removeAllViews();
        LayoutInflater inflater = getLayoutInflater();
        int savedCount = 0;

        for (Product p : appState.getCatalog()) {
            if (appState.isWishlisted(p.getId())) {
                savedCount++;
                View card = inflater.inflate(R.layout.item_catalog_card, container, false);
                ImageView img = card.findViewById(R.id.catalogItemImage);
                TextView tag = card.findViewById(R.id.catalogItemTag);
                TextView title = card.findViewById(R.id.catalogItemTitle);
                TextView rating = card.findViewById(R.id.catalogItemRating);
                TextView price = card.findViewById(R.id.catalogItemPrice);
                MaterialButton btnView = card.findViewById(R.id.btnViewCatalogProduct);

                img.setImageResource(p.getImageResId());
                tag.setText(p.getTag());
                title.setText(p.getName());
                rating.setText("★ " + p.getRating() + " · Saved");
                price.setText("₹" + p.getPrice());
                btnView.setText("View / Add");

                View.OnClickListener openDetail = v -> {
                    appState.setActiveProduct(p);
                    activity.navigateTo(new ProductDetailFragment(), true);
                };

                btnView.setOnClickListener(openDetail);
                card.setOnClickListener(openDetail);

                container.addView(card);
            }
        }

        if (savedCount == 0) {
            emptyLayout.setVisibility(View.VISIBLE);
            countText.setText("No saved items yet");
        } else {
            emptyLayout.setVisibility(View.GONE);
            countText.setText(savedCount + " saved item(s) for home delivery or boutique fitting");
        }
    }
}
