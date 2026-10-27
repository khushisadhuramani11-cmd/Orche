package com.acadhub.orche;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.snackbar.Snackbar;

public class ProductDetailFragment extends Fragment {

    private final AppState appState = AppState.getInstance();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.screen_product_detail, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View root, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(root, savedInstanceState);

        MainActivity activity = (MainActivity) requireActivity();

        Product product = appState.getActiveProduct();
        if (product == null) {
            product = appState.getProductById("p_bra_01");
            appState.setActiveProduct(product);
        }
        final Product currentProduct = product;

        // Back button
        root.findViewById(R.id.backButton).setOnClickListener(v -> activity.onBackPressed());

        // Wishlist button
        ImageView btnWishlist = root.findViewById(R.id.btnWishlistToggle);
        boolean isSaved = appState.isWishlisted(product.getId());
        btnWishlist.setImageResource(isSaved ? R.drawable.ic_heart_filled : R.drawable.ic_heart);
        btnWishlist.setOnClickListener(v -> {
            boolean nowSaved = appState.toggleWishlist(currentProduct.getId());
            btnWishlist.setImageResource(nowSaved ? R.drawable.ic_heart_filled : R.drawable.ic_heart);
            Toast.makeText(activity, nowSaved ? "Saved to wishlist" : "Removed from wishlist", Toast.LENGTH_SHORT).show();
        });

        // Hero image & details
        ImageView heroImage = root.findViewById(R.id.productHeroImage);
        heroImage.setImageResource(product.getImageResId());

        TextView txtTag = root.findViewById(R.id.productTag);
        txtTag.setText(product.getTag());

        TextView txtTitle = root.findViewById(R.id.productTitle);
        txtTitle.setText(product.getName());

        TextView txtPrice = root.findViewById(R.id.productPrice);
        txtPrice.setText("₹" + product.getPrice());

        TextView txtInStoreStock = root.findViewById(R.id.txtInStoreStock);
        txtInStoreStock.setText(product.getInStoreStock());

        // Color selection
        TextView txtColorLabel = root.findViewById(R.id.txtSelectedColorLabel);
        View swatchRose = root.findViewById(R.id.swatchRose);
        View swatchIvory = root.findViewById(R.id.swatchIvory);
        View swatchPlum = root.findViewById(R.id.swatchPlum);
        View swatchMocha = root.findViewById(R.id.swatchMocha);

        Runnable updateSwatches = () -> {
            String activeCol = appState.getActiveColor();
            txtColorLabel.setText("COLOUR · " + activeCol);

            swatchRose.setBackgroundResource(activeCol.equals("Rose Nude") ? R.drawable.bg_chip_selected : R.drawable.bg_chip_unselected);
            swatchIvory.setBackgroundResource(activeCol.equals("Silk Ivory") ? R.drawable.bg_chip_selected : R.drawable.bg_chip_unselected);
            swatchPlum.setBackgroundResource(activeCol.equals("Deep Plum") ? R.drawable.bg_chip_selected : R.drawable.bg_chip_unselected);
            swatchMocha.setBackgroundResource(activeCol.equals("Mocha") ? R.drawable.bg_chip_selected : R.drawable.bg_chip_unselected);
        };

        swatchRose.setOnClickListener(v -> { appState.setActiveColor("Rose Nude"); updateSwatches.run(); });
        swatchIvory.setOnClickListener(v -> { appState.setActiveColor("Silk Ivory"); updateSwatches.run(); });
        swatchPlum.setOnClickListener(v -> { appState.setActiveColor("Deep Plum"); updateSwatches.run(); });
        swatchMocha.setOnClickListener(v -> { appState.setActiveColor("Mocha"); updateSwatches.run(); });

        updateSwatches.run();

        // Size pills
        TextView pill30B = root.findViewById(R.id.pillSize30B);
        TextView pill32B = root.findViewById(R.id.pillSize32B);
        TextView pill34B = root.findViewById(R.id.pillSize34B);
        TextView pill36B = root.findViewById(R.id.pillSize36B);
        TextView pill38B = root.findViewById(R.id.pillSize38B);

        TextView[] sizePills = new TextView[]{pill30B, pill32B, pill34B, pill36B, pill38B};
        String[] sizeNames = new String[]{"30B", "32B", "34B", "36B", "38B"};

        Runnable updateSizes = () -> {
            String currentSize = appState.getActiveSize();
            for (int i = 0; i < sizePills.length; i++) {
                boolean isSelected = sizeNames[i].equalsIgnoreCase(currentSize);
                sizePills[i].setBackgroundResource(isSelected ? R.drawable.bg_chip_selected : R.drawable.bg_chip_unselected);
                sizePills[i].setTextColor(activity.getColor(isSelected ? R.color.orche_white : R.color.orche_charcoal));
            }
        };

        for (int i = 0; i < sizePills.length; i++) {
            final String sizeVal = sizeNames[i];
            sizePills[i].setOnClickListener(v -> {
                appState.setActiveSize(sizeVal);
                updateSizes.run();
            });
        }
        updateSizes.run();

        // Size Guide link -> opens SizeGuideFragment
        root.findViewById(R.id.btnOpenSizeGuide).setOnClickListener(v -> activity.navigateTo(new SizeGuideFragment(), true));

        // Links to Reviews & Hygiene Policy
        View btnReviews = root.findViewById(R.id.btnOpenProductReviews);
        if (btnReviews != null) {
            btnReviews.setOnClickListener(v -> activity.navigateTo(new ProductReviewsFragment(), true));
        }

        View btnHygiene = root.findViewById(R.id.btnOpenHygienePolicy);
        if (btnHygiene != null) {
            btnHygiene.setOnClickListener(v -> activity.navigateTo(new HygienePolicyFragment(), true));
        }

        // In-Store Fitting Room Button
        root.findViewById(R.id.btnSendToFittingRoom).setOnClickListener(v -> {
            appState.setLastFittingRoomRequest(currentProduct.getName() + " (" + appState.getActiveSize() + ", " + appState.getActiveColor() + ")");
            new AlertDialog.Builder(activity)
                    .setTitle("Fitting Room Service")
                    .setMessage("Requested " + appState.getActiveSize() + " in " + appState.getActiveColor() + " to " + appState.getActiveFittingRoom() + ".\n\nAn Orche' boutique associate is bringing this item now.")
                    .setPositiveButton("Got it", null)
                    .show();
        });

        // Add to Bag Button
        MaterialButton btnAddToBag = root.findViewById(R.id.addToBagButton);
        btnAddToBag.setText("Add to bag · ₹" + product.getPrice());
        btnAddToBag.setOnClickListener(v -> {
            appState.addToCart(currentProduct, appState.getActiveColor(), appState.getActiveSize());
            activity.updateGlobalCartBadge();
            Snackbar snackbar = Snackbar.make(root, "Added " + currentProduct.getName() + " to bag", Snackbar.LENGTH_LONG)
                    .setAction("View Bag", actView -> activity.navigateTo(new CartFragment(), true));
            snackbar.show();
        });
    }
}
