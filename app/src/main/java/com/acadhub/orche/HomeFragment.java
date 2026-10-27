package com.acadhub.orche;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class HomeFragment extends Fragment {

    private final AppState appState = AppState.getInstance();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.screen_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View root, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(root, savedInstanceState);

        MainActivity activity = (MainActivity) requireActivity();

        TextView modeDeliveryBtn = root.findViewById(R.id.modeDeliveryBtn);
        TextView modeInStoreBtn = root.findViewById(R.id.modeInStoreBtn);
        View inStoreBannerCard = root.findViewById(R.id.inStoreBannerCard);
        TextView headerCartBadge = root.findViewById(R.id.headerCartBadge);

        // Header Cart Badge
        int count = appState.getCartItemCount();
        if (headerCartBadge != null) {
            if (count > 0) {
                headerCartBadge.setVisibility(View.VISIBLE);
                headerCartBadge.setText(String.valueOf(count));
            } else {
                headerCartBadge.setVisibility(View.GONE);
            }
        }

        // Header Icon Clicks
        root.findViewById(R.id.headerWishlistBtn).setOnClickListener(v -> activity.navigateTo(new SavedFragment(), true));
        root.findViewById(R.id.headerBagBtn).setOnClickListener(v -> activity.navigateTo(new CartFragment(), true));

        // Omnichannel Mode Switcher (McDonald's Retail Style)
        Runnable updateModeUI = () -> {
            boolean isDelivery = appState.getFulfillmentMode() == AppState.FulfillmentMode.DELIVERY;
            if (isDelivery) {
                modeDeliveryBtn.setBackgroundResource(R.drawable.bg_chip_selected);
                modeDeliveryBtn.setTextColor(activity.getColor(R.color.orche_white));
                modeInStoreBtn.setBackground(null);
                modeInStoreBtn.setTextColor(activity.getColor(R.color.orche_plum));
                inStoreBannerCard.setVisibility(View.GONE);
            } else {
                modeInStoreBtn.setBackgroundResource(R.drawable.bg_chip_selected);
                modeInStoreBtn.setTextColor(activity.getColor(R.color.orche_white));
                modeDeliveryBtn.setBackground(null);
                modeDeliveryBtn.setTextColor(activity.getColor(R.color.orche_plum));
                inStoreBannerCard.setVisibility(View.VISIBLE);
            }
        };

        modeDeliveryBtn.setOnClickListener(v -> {
            appState.setFulfillmentMode(AppState.FulfillmentMode.DELIVERY);
            updateModeUI.run();
        });

        modeInStoreBtn.setOnClickListener(v -> {
            appState.setFulfillmentMode(AppState.FulfillmentMode.INSTORE_PICKUP);
            updateModeUI.run();
        });

        updateModeUI.run();

        // In-Store Quick Scan
        View btnQuickScan = root.findViewById(R.id.btnQuickScan);
        if (btnQuickScan != null) {
            btnQuickScan.setOnClickListener(v -> activity.showInStoreScannerDialog());
        }

        // Shop the collection CTA
        root.findViewById(R.id.shopButton).setOnClickListener(v -> {
            activity.setActiveExploreFilter("All");
            activity.navigateTo(new ExploreFragment(), true);
        });

        // View All Categories
        root.findViewById(R.id.btnViewAllCategories).setOnClickListener(v -> {
            activity.setActiveExploreFilter("All");
            activity.navigateTo(new ExploreFragment(), true);
        });

        // Categories Click
        root.findViewById(R.id.catBras).setOnClickListener(v -> {
            activity.setActiveExploreFilter("Bras");
            activity.navigateTo(new ExploreFragment(), true);
        });
        root.findViewById(R.id.catBriefs).setOnClickListener(v -> {
            activity.setActiveExploreFilter("Briefs");
            activity.navigateTo(new ExploreFragment(), true);
        });
        root.findViewById(R.id.catSleepwear).setOnClickListener(v -> {
            activity.setActiveExploreFilter("Sleepwear");
            activity.navigateTo(new ExploreFragment(), true);
        });
        root.findViewById(R.id.catSets).setOnClickListener(v -> {
            activity.setActiveExploreFilter("Sets");
            activity.navigateTo(new ExploreFragment(), true);
        });

        // Bra Fit Calculator Quick Access Card -> launches Size Guide / Fit Finder
        root.findViewById(R.id.btnFitCalculatorHome).setOnClickListener(v -> activity.navigateTo(new SizeGuideFragment(), true));

        // Bestseller Product Cards
        root.findViewById(R.id.cardBestsellerBra).setOnClickListener(v -> {
            appState.setActiveProduct(appState.getProductById("p_bra_01"));
            activity.navigateTo(new ProductDetailFragment(), true);
        });
        root.findViewById(R.id.cardBestsellerBrief).setOnClickListener(v -> {
            appState.setActiveProduct(appState.getProductById("p_brief_01"));
            activity.navigateTo(new ProductDetailFragment(), true);
        });
        root.findViewById(R.id.cardBestsellerBralette).setOnClickListener(v -> {
            appState.setActiveProduct(appState.getProductById("p_bralette_02"));
            activity.navigateTo(new ProductDetailFragment(), true);
        });
        root.findViewById(R.id.cardBestsellerSleep).setOnClickListener(v -> {
            appState.setActiveProduct(appState.getProductById("p_sleep_01"));
            activity.navigateTo(new ProductDetailFragment(), true);
        });
    }
}
