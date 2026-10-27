package com.acadhub.orche;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.material.button.MaterialButton;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class ExploreFragment extends Fragment {

    private final AppState appState = AppState.getInstance();
    private String activeCategoryFilter = "All";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.screen_explore, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View root, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(root, savedInstanceState);

        MainActivity activity = (MainActivity) requireActivity();
        activeCategoryFilter = activity.getActiveExploreFilter();

        LinearLayout container = root.findViewById(R.id.catalogListContainer);
        EditText searchEdit = root.findViewById(R.id.editCatalogSearch);
        View btnFilterSort = root.findViewById(R.id.btnOpenFilterSort);

        TextView chipAll = root.findViewById(R.id.chipFilterAll);
        TextView chipBras = root.findViewById(R.id.chipFilterBras);
        TextView chipBriefs = root.findViewById(R.id.chipFilterBriefs);
        TextView chipSleep = root.findViewById(R.id.chipFilterSleepwear);
        TextView chipSets = root.findViewById(R.id.chipFilterSets);

        TextView[] chips = new TextView[]{chipAll, chipBras, chipBriefs, chipSleep, chipSets};
        String[] chipNames = new String[]{"All", "Bras", "Briefs", "Sleepwear", "Sets"};

        Runnable populateCatalog = new Runnable() {
            @Override
            public void run() {
                container.removeAllViews();
                LayoutInflater inflater = getLayoutInflater();
                String searchQuery = searchEdit.getText().toString().trim().toLowerCase();

                List<Product> products = new ArrayList<>(appState.getCatalog());

                // Apply Sorting
                String sort = appState.getActiveSort();
                if ("PriceLowHigh".equalsIgnoreCase(sort)) {
                    Collections.sort(products, Comparator.comparingInt(Product::getPrice));
                } else if ("PriceHighLow".equalsIgnoreCase(sort)) {
                    Collections.sort(products, (a, b) -> Integer.compare(b.getPrice(), a.getPrice()));
                } else if ("Rating".equalsIgnoreCase(sort)) {
                    Collections.sort(products, (a, b) -> Double.compare(b.getRating(), a.getRating()));
                }

                for (Product p : products) {
                    boolean categoryMatch = activeCategoryFilter.equals("All") || p.getCategory().equalsIgnoreCase(activeCategoryFilter);
                    boolean searchMatch = searchQuery.isEmpty()
                            || p.getName().toLowerCase().contains(searchQuery)
                            || p.getCategory().toLowerCase().contains(searchQuery);
                    boolean priceMatch = p.getPrice() <= appState.getFilterMaxPrice();

                    if (categoryMatch && searchMatch && priceMatch) {
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
                        rating.setText("★ " + p.getRating() + " · " + p.getReviews());
                        price.setText("₹" + p.getPrice());

                        View.OnClickListener openDetail = v -> {
                            appState.setActiveProduct(p);
                            activity.navigateTo(new ProductDetailFragment(), true);
                        };

                        btnView.setOnClickListener(openDetail);
                        card.setOnClickListener(openDetail);

                        container.addView(card);
                    }
                }
            }
        };

        // Filter & Sort Modal Trigger
        btnFilterSort.setOnClickListener(v -> activity.showFilterSortDialog(populateCatalog));

        for (int i = 0; i < chips.length; i++) {
            final String catName = chipNames[i];
            final TextView chip = chips[i];
            chip.setOnClickListener(v -> {
                activeCategoryFilter = catName;
                activity.setActiveExploreFilter(catName);
                for (TextView c : chips) {
                    c.setBackgroundResource(R.drawable.bg_chip_unselected);
                    c.setTextColor(activity.getColor(R.color.orche_charcoal));
                }
                chip.setBackgroundResource(R.drawable.bg_chip_selected);
                chip.setTextColor(activity.getColor(R.color.orche_white));
                populateCatalog.run();
            });
        }

        // Apply pre-selected filter styling
        for (int i = 0; i < chips.length; i++) {
            if (chipNames[i].equalsIgnoreCase(activeCategoryFilter)) {
                chips[i].setBackgroundResource(R.drawable.bg_chip_selected);
                chips[i].setTextColor(activity.getColor(R.color.orche_white));
            } else {
                chips[i].setBackgroundResource(R.drawable.bg_chip_unselected);
                chips[i].setTextColor(activity.getColor(R.color.orche_charcoal));
            }
        }

        searchEdit.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override public void onTextChanged(CharSequence s, int start, int before, int count) { populateCatalog.run(); }
            @Override public void afterTextChanged(Editable s) {}
        });

        populateCatalog.run();
    }
}
