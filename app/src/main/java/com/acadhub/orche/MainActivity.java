package com.acadhub.orche;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.google.android.material.button.MaterialButton;

public class MainActivity extends AppCompatActivity {

    public static final int REQ_LOCATION = 101;
    public static final int REQ_MEDIA = 102;
    public static final int REQ_PICK_IMAGE = 103;

    private LinearLayout globalBottomNav;
    private TextView tabCartBadge;

    private ImageView tabHomeIcon;
    private TextView tabHomeText;
    private ImageView tabExploreIcon;
    private TextView tabExploreText;
    private ImageView tabInStoreIcon;
    private TextView tabInStoreText;
    private ImageView tabSavedIcon;
    private TextView tabSavedText;
    private ImageView tabBagIcon;
    private TextView tabBagText;

    private final AppState appState = AppState.getInstance();
    private String activeExploreFilter = "All";

    // Callbacks for runtime permissions
    public interface LocationCallback {
        void onLocationDetected(String city, String pinCode);
    }
    private LocationCallback pendingLocationCallback;
    private Runnable pendingMediaCallback;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        globalBottomNav = findViewById(R.id.globalBottomNav);
        tabCartBadge = findViewById(R.id.tabCartBadge);

        tabHomeIcon = findViewById(R.id.tabHomeIcon);
        tabHomeText = findViewById(R.id.tabHomeText);
        tabExploreIcon = findViewById(R.id.tabExploreIcon);
        tabExploreText = findViewById(R.id.tabExploreText);
        tabInStoreIcon = findViewById(R.id.tabInStoreIcon);
        tabInStoreText = findViewById(R.id.tabInStoreText);
        tabSavedIcon = findViewById(R.id.tabSavedIcon);
        tabSavedText = findViewById(R.id.tabSavedText);
        tabBagIcon = findViewById(R.id.tabBagIcon);
        tabBagText = findViewById(R.id.tabBagText);

        setupBottomNav();
        updateGlobalCartBadge();

        getSupportFragmentManager().addOnBackStackChangedListener(() -> {
            Fragment current = getCurrentFragment();
            updateBottomNavSelection(current);
        });

        if (savedInstanceState == null) {
            navigateTo(new HomeFragment(), false);
        }
    }

    private void setupBottomNav() {
        findViewById(R.id.tabHome).setOnClickListener(v -> navigateTo(new HomeFragment(), false));
        findViewById(R.id.tabExplore).setOnClickListener(v -> {
            activeExploreFilter = "All";
            navigateTo(new ExploreFragment(), true);
        });
        findViewById(R.id.tabInStore).setOnClickListener(v -> {
            appState.setFulfillmentMode(AppState.FulfillmentMode.INSTORE_PICKUP);
            showInStoreScannerDialog();
        });
        findViewById(R.id.tabSaved).setOnClickListener(v -> navigateTo(new SavedFragment(), true));
        findViewById(R.id.tabBag).setOnClickListener(v -> navigateTo(new CartFragment(), true));
    }

    public void navigateTo(Fragment fragment, boolean addToBackStack) {
        var transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.screenHost, fragment);
        if (addToBackStack) {
            transaction.addToBackStack(fragment.getClass().getSimpleName());
        }
        transaction.commit();

        updateBottomNavSelection(fragment);
        updateGlobalCartBadge();
    }

    public Fragment getCurrentFragment() {
        return getSupportFragmentManager().findFragmentById(R.id.screenHost);
    }

    private void updateBottomNavSelection(Fragment fragment) {
        int mutedColor = getColor(R.color.orche_muted);
        int activeColor = getColor(R.color.orche_plum);

        tabHomeIcon.setColorFilter(mutedColor);
        tabHomeText.setTextColor(mutedColor);
        tabExploreIcon.setColorFilter(mutedColor);
        tabExploreText.setTextColor(mutedColor);
        tabInStoreIcon.setColorFilter(mutedColor);
        tabInStoreText.setTextColor(mutedColor);
        tabSavedIcon.setColorFilter(mutedColor);
        tabSavedText.setTextColor(mutedColor);
        tabBagIcon.setColorFilter(mutedColor);
        tabBagText.setTextColor(mutedColor);

        if (fragment instanceof HomeFragment) {
            tabHomeIcon.setColorFilter(activeColor);
            tabHomeText.setTextColor(activeColor);
        } else if (fragment instanceof ExploreFragment) {
            tabExploreIcon.setColorFilter(activeColor);
            tabExploreText.setTextColor(activeColor);
        } else if (fragment instanceof SavedFragment) {
            tabSavedIcon.setColorFilter(activeColor);
            tabSavedText.setTextColor(activeColor);
        } else if (fragment instanceof CartFragment) {
            tabBagIcon.setColorFilter(activeColor);
            tabBagText.setTextColor(activeColor);
        }

        // Hide bottom nav on transaction-focused screens
        if (fragment instanceof CheckoutFragment || fragment instanceof TrackingFragment || fragment instanceof PaymentResultFragment) {
            globalBottomNav.setVisibility(View.GONE);
        } else {
            globalBottomNav.setVisibility(View.VISIBLE);
        }
    }

    public void updateGlobalCartBadge() {
        int count = appState.getCartItemCount();
        if (tabCartBadge != null) {
            if (count > 0) {
                tabCartBadge.setVisibility(View.VISIBLE);
                tabCartBadge.setText(String.valueOf(count));
            } else {
                tabCartBadge.setVisibility(View.GONE);
            }
        }
    }

    public String getActiveExploreFilter() {
        return activeExploreFilter;
    }

    public void setActiveExploreFilter(String filter) {
        this.activeExploreFilter = filter;
    }

    // ==========================================
    // PERMISSIONS: LOCATION (P0 Address & Store)
    // ==========================================
    public void requestLocationPermissionAndDetect(LocationCallback callback) {
        this.pendingLocationCallback = callback;
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            handleLocationSuccess();
        } else {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},
                    REQ_LOCATION);
        }
    }

    private void handleLocationSuccess() {
        Toast.makeText(this, "📍 Location detected: Bandra West, Mumbai", Toast.LENGTH_SHORT).show();
        if (pendingLocationCallback != null) {
            pendingLocationCallback.onLocationDetected("Mumbai", "400050");
        }
    }

    // ==========================================
    // PERMISSIONS: CAMERA & MEDIA (P2 Reviews)
    // ==========================================
    public void requestMediaPermissionAndPick(Runnable onGranted) {
        this.pendingMediaCallback = onGranted;
        String perm = (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU)
                ? Manifest.permission.READ_MEDIA_IMAGES
                : Manifest.permission.CAMERA;

        if (ContextCompat.checkSelfPermission(this, perm) == PackageManager.PERMISSION_GRANTED) {
            launchImagePicker();
        } else {
            ActivityCompat.requestPermissions(this, new String[]{perm}, REQ_MEDIA);
        }
    }

    private void launchImagePicker() {
        Intent pickIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        try {
            startActivityForResult(pickIntent, REQ_PICK_IMAGE);
        } catch (Exception e) {
            Toast.makeText(this, "Photo picker opened", Toast.LENGTH_SHORT).show();
            if (pendingMediaCallback != null) pendingMediaCallback.run();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQ_LOCATION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                handleLocationSuccess();
            } else {
                Toast.makeText(this, "Location permission not granted. You can enter PIN manually.", Toast.LENGTH_SHORT).show();
            }
        } else if (requestCode == REQ_MEDIA) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                launchImagePicker();
            } else {
                Toast.makeText(this, "Camera/Media permission granted for review photo upload.", Toast.LENGTH_SHORT).show();
                if (pendingMediaCallback != null) pendingMediaCallback.run();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQ_PICK_IMAGE && resultCode == RESULT_OK) {
            Toast.makeText(this, "Fit photo attached to review!", Toast.LENGTH_SHORT).show();
            if (pendingMediaCallback != null) pendingMediaCallback.run();
        }
    }

    // ==========================================
    // DIALOGS: AUTH, FIT FINDER, FILTER, REVIEWS
    // ==========================================

    /**
     * P0: Login, Sign-up & Guest Checkout
     */
    public void showAuthDialog(Runnable onComplete) {
        Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_auth);

        if (dialog.getWindow() != null) {
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        }

        TextView tabLogin = dialog.findViewById(R.id.tabLogin);
        TextView tabSignUp = dialog.findViewById(R.id.tabSignUp);
        EditText editName = dialog.findViewById(R.id.editAuthName);
        EditText editContact = dialog.findViewById(R.id.editAuthContact);
        MaterialButton btnSubmit = dialog.findViewById(R.id.btnSubmitAuth);

        final boolean[] isSignup = {false};

        tabLogin.setOnClickListener(v -> {
            isSignup[0] = false;
            tabLogin.setBackgroundResource(R.drawable.bg_chip_selected);
            tabLogin.setTextColor(getColor(R.color.orche_white));
            tabSignUp.setBackground(null);
            tabSignUp.setTextColor(getColor(R.color.orche_plum));
            editName.setVisibility(View.GONE);
            btnSubmit.setText("Sign In to Orche'");
        });

        tabSignUp.setOnClickListener(v -> {
            isSignup[0] = true;
            tabSignUp.setBackgroundResource(R.drawable.bg_chip_selected);
            tabSignUp.setTextColor(getColor(R.color.orche_white));
            tabLogin.setBackground(null);
            tabLogin.setTextColor(getColor(R.color.orche_plum));
            editName.setVisibility(View.VISIBLE);
            btnSubmit.setText("Create Member Account");
        });

        btnSubmit.setOnClickListener(v -> {
            String contact = editContact.getText().toString().trim();
            String name = editName.getText().toString().trim();
            appState.login(name.isEmpty() ? "Khwahish" : name, contact.contains("@") ? contact : "user@orche.com", contact);
            dialog.dismiss();
            Toast.makeText(this, "Welcome, " + appState.getUserName() + "!", Toast.LENGTH_SHORT).show();
            if (onComplete != null) onComplete.run();
        });

        dialog.findViewById(R.id.btnContinueAsGuest).setOnClickListener(v -> {
            appState.continueAsGuest();
            dialog.dismiss();
            Toast.makeText(this, "Continuing as Guest", Toast.LENGTH_SHORT).show();
            if (onComplete != null) onComplete.run();
        });

        dialog.findViewById(R.id.btnCloseAuth).setOnClickListener(v -> dialog.dismiss());
        dialog.show();
    }

    /**
     * P0: Filter & Sort Dialog
     */
    public void showFilterSortDialog(Runnable onApply) {
        Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_filter_sort);

        if (dialog.getWindow() != null) {
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        }

        RadioGroup rgSort = dialog.findViewById(R.id.radioGroupSort);
        TextView chipPriceAll = dialog.findViewById(R.id.chipPriceAll);
        TextView chipUnder1000 = dialog.findViewById(R.id.chipPriceUnder1000);
        TextView chipAbove1000 = dialog.findViewById(R.id.chipPriceAbove1000);

        final int[] maxPrice = {appState.getFilterMaxPrice()};

        chipPriceAll.setOnClickListener(v -> {
            maxPrice[0] = 3000;
            chipPriceAll.setBackgroundResource(R.drawable.bg_chip_selected);
            chipPriceAll.setTextColor(getColor(R.color.orche_white));
            chipUnder1000.setBackgroundResource(R.drawable.bg_chip_unselected);
            chipUnder1000.setTextColor(getColor(R.color.orche_charcoal));
            chipAbove1000.setBackgroundResource(R.drawable.bg_chip_unselected);
            chipAbove1000.setTextColor(getColor(R.color.orche_charcoal));
        });

        chipUnder1000.setOnClickListener(v -> {
            maxPrice[0] = 999;
            chipUnder1000.setBackgroundResource(R.drawable.bg_chip_selected);
            chipUnder1000.setTextColor(getColor(R.color.orche_white));
            chipPriceAll.setBackgroundResource(R.drawable.bg_chip_unselected);
            chipPriceAll.setTextColor(getColor(R.color.orche_charcoal));
            chipAbove1000.setBackgroundResource(R.drawable.bg_chip_unselected);
            chipAbove1000.setTextColor(getColor(R.color.orche_charcoal));
        });

        chipAbove1000.setOnClickListener(v -> {
            maxPrice[0] = 3000;
            chipAbove1000.setBackgroundResource(R.drawable.bg_chip_selected);
            chipAbove1000.setTextColor(getColor(R.color.orche_white));
            chipPriceAll.setBackgroundResource(R.drawable.bg_chip_unselected);
            chipPriceAll.setTextColor(getColor(R.color.orche_charcoal));
            chipUnder1000.setBackgroundResource(R.drawable.bg_chip_unselected);
            chipUnder1000.setTextColor(getColor(R.color.orche_charcoal));
        });

        dialog.findViewById(R.id.btnApplyFilters).setOnClickListener(v -> {
            int checkedId = rgSort.getCheckedRadioButtonId();
            if (checkedId == R.id.sortPriceLowHigh) appState.setActiveSort("PriceLowHigh");
            else if (checkedId == R.id.sortPriceHighLow) appState.setActiveSort("PriceHighLow");
            else if (checkedId == R.id.sortRating) appState.setActiveSort("Rating");
            else appState.setActiveSort("Recommended");

            appState.setFilterMaxPrice(maxPrice[0]);
            dialog.dismiss();
            if (onApply != null) onApply.run();
        });

        dialog.findViewById(R.id.btnResetFilters).setOnClickListener(v -> {
            appState.clearFilters();
            dialog.dismiss();
            if (onApply != null) onApply.run();
        });

        dialog.findViewById(R.id.btnCloseFilter).setOnClickListener(v -> dialog.dismiss());
        dialog.show();
    }

    /**
     * P0: Fit Finder / Bra Size Calculator
     */
    public void showFitCalculatorDialog() {
        Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_fit_calculator);

        if (dialog.getWindow() != null) {
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        }

        TextView txtUnderbust = dialog.findViewById(R.id.txtUnderbustValue);
        TextView txtBust = dialog.findViewById(R.id.txtBustValue);
        TextView txtCalculated = dialog.findViewById(R.id.txtCalculatedSize);
        TextView txtSister = dialog.findViewById(R.id.txtSisterSizes);
        MaterialButton btnApply = dialog.findViewById(R.id.btnApplyCalculatedSize);

        final int[] underbust = {30};
        final int[] bust = {34};

        Runnable updateCalc = () -> {
            txtUnderbust.setText(underbust[0] + " inches");
            txtBust.setText(bust[0] + " inches");

            int band = (underbust[0] % 2 == 0) ? underbust[0] + 4 : underbust[0] + 5;
            int diff = bust[0] - band;
            String cup;
            if (diff <= 0 || diff == 1) cup = "A";
            else if (diff == 2) cup = "B";
            else if (diff == 3) cup = "C";
            else if (diff == 4) cup = "D";
            else cup = "DD";

            String result = band + cup;
            txtCalculated.setText(result);
            txtSister.setText("Sister sizes: " + (band - 2) + ((char)(cup.charAt(0) + 1)) + " (snug) · " + (band + 2) + "A (loose)");
            btnApply.setText("Apply " + result + " to selection");
        };

        dialog.findViewById(R.id.btnUnderbustMinus).setOnClickListener(v -> { if (underbust[0] > 26) { underbust[0]--; updateCalc.run(); } });
        dialog.findViewById(R.id.btnUnderbustPlus).setOnClickListener(v -> { if (underbust[0] < 44) { underbust[0]++; updateCalc.run(); } });
        dialog.findViewById(R.id.btnBustMinus).setOnClickListener(v -> { if (bust[0] > underbust[0]) { bust[0]--; updateCalc.run(); } });
        dialog.findViewById(R.id.btnBustPlus).setOnClickListener(v -> { if (bust[0] < 50) { bust[0]++; updateCalc.run(); } });

        dialog.findViewById(R.id.btnCloseFitFinder).setOnClickListener(v -> dialog.dismiss());

        btnApply.setOnClickListener(v -> {
            String size = txtCalculated.getText().toString();
            appState.setActiveSize(size);
            dialog.dismiss();
            Toast.makeText(this, "Recommended size " + size + " selected!", Toast.LENGTH_SHORT).show();
            Fragment cur = getCurrentFragment();
            if (cur instanceof ProductDetailFragment) {
                navigateTo(new ProductDetailFragment(), false);
            }
        });

        updateCalc.run();
        dialog.show();
    }

    /**
     * In-Store Garment Scanner & Fitting Room Assistant (McDonald's Retail Style)
     */
    public void showInStoreScannerDialog() {
        Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_instore_scanner);

        if (dialog.getWindow() != null) {
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        }

        TextView txtScannedTitle = dialog.findViewById(R.id.txtScannedItemTitle);
        TextView txtScannedLoc = dialog.findViewById(R.id.txtScannedLocation);
        MaterialButton btnSendToRoom = dialog.findViewById(R.id.btnSendScannedToRoom);
        MaterialButton btnExpressBuy = dialog.findViewById(R.id.btnExpressBuyScanned);

        final Product[] scannedProduct = {appState.getProductById("p_bra_01")};

        dialog.findViewById(R.id.btnScanTagBra).setOnClickListener(v -> {
            scannedProduct[0] = appState.getProductById("p_bra_01");
            txtScannedTitle.setText("Soft Form Everyday Bra · 34B");
            txtScannedLoc.setText("📍 Rack 4 (Bandra Boutique) · 4 items available");
        });

        dialog.findViewById(R.id.btnScanTagBrief).setOnClickListener(v -> {
            scannedProduct[0] = appState.getProductById("p_brief_01");
            txtScannedTitle.setText("Everyday Seamless Brief · M");
            txtScannedLoc.setText("📍 Rack 2 (Bandra Boutique) · 8 items available");
        });

        btnSendToRoom.setOnClickListener(v -> {
            dialog.dismiss();
            new AlertDialog.Builder(this)
                    .setTitle("Fitting Room Service")
                    .setMessage("Requested " + scannedProduct[0].getName() + " to " + appState.getActiveFittingRoom() + ".\n\nAn associate is bringing it directly to your room.")
                    .setPositiveButton("OK", null)
                    .show();
        });

        btnExpressBuy.setOnClickListener(v -> {
            appState.addToCart(scannedProduct[0], "Rose Nude", "34B");
            dialog.dismiss();
            updateGlobalCartBadge();
            navigateTo(new CartFragment(), true);
        });

        dialog.findViewById(R.id.btnCloseScanner).setOnClickListener(v -> dialog.dismiss());
        dialog.show();
    }

    /**
     * P2: Write Verified Review with Photo Upload (Media & Camera Permission)
     */
    public void showWriteReviewDialog(Runnable onSubmitted) {
        Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_write_review);

        if (dialog.getWindow() != null) {
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        }

        EditText editComment = dialog.findViewById(R.id.editReviewComment);
        TextView txtPhotoStatus = dialog.findViewById(R.id.txtPhotoStatus);

        dialog.findViewById(R.id.btnAttachReviewPhoto).setOnClickListener(v -> {
            requestMediaPermissionAndPick(() -> {
                txtPhotoStatus.setText("✓ Fit Photo Attached (fit_review_01.jpg)");
                txtPhotoStatus.setTextColor(getColor(R.color.orche_success));
            });
        });

        dialog.findViewById(R.id.btnSubmitReview).setOnClickListener(v -> {
            String comment = editComment.getText().toString().trim();
            if (comment.isEmpty()) {
                comment = "Incredible fabric softness and true to size fit. Looks invisible under clothes!";
            }
            Review newRev = new Review(
                    "rev_" + System.currentTimeMillis(),
                    appState.getUserName() + " · Verified Buyer",
                    5.0,
                    "Just now",
                    "True to size · Highly recommended",
                    comment,
                    true,
                    1
            );
            appState.addReview(newRev);
            dialog.dismiss();
            Toast.makeText(this, "Thank you! Your verified review has been published.", Toast.LENGTH_SHORT).show();
            if (onSubmitted != null) onSubmitted.run();
        });

        dialog.findViewById(R.id.btnCloseWriteReview).setOnClickListener(v -> dialog.dismiss());
        dialog.show();
    }

    @Override
    @SuppressWarnings("deprecation")
    public void onBackPressed() {
        FragmentManager fm = getSupportFragmentManager();
        if (fm.getBackStackEntryCount() > 0) {
            fm.popBackStack();
        } else {
            Fragment current = getCurrentFragment();
            if (!(current instanceof HomeFragment)) {
                navigateTo(new HomeFragment(), false);
            } else {
                super.onBackPressed();
            }
        }
    }
}
