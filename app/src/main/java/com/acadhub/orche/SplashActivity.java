package com.acadhub.orche;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;

import androidx.appcompat.app.AppCompatActivity;

@SuppressLint("CustomSplashScreen")
public class SplashActivity extends AppCompatActivity {

    private final Handler handler = new Handler(Looper.getMainLooper());
    private Runnable launchRunnable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        View logo = findViewById(R.id.splashLogoImage);
        View subtitle = findViewById(R.id.splashSubtitle);
        View footer = findViewById(R.id.splashFooter);

        // Initial state for graceful entry animation
        if (logo != null) {
            logo.setScaleX(0.90f);
            logo.setScaleY(0.90f);
            logo.setAlpha(0f);
            logo.setTranslationY(24f);
            logo.animate()
                    .alpha(1f)
                    .translationY(0f)
                    .scaleX(1.0f)
                    .scaleY(1.0f)
                    .setDuration(950)
                    .setInterpolator(new AccelerateDecelerateInterpolator())
                    .start();
        }

        if (subtitle != null) {
            subtitle.setAlpha(0f);
            subtitle.animate()
                    .alpha(1f)
                    .setDuration(900)
                    .setStartDelay(300)
                    .start();
        }

        if (footer != null) {
            footer.setAlpha(0f);
            footer.animate()
                    .alpha(1f)
                    .setDuration(900)
                    .setStartDelay(450)
                    .start();
        }

        launchRunnable = this::proceedToMain;

        // Auto transition after 2.3 seconds
        handler.postDelayed(launchRunnable, 2300);

        // Tap to skip immediately
        findViewById(R.id.splashCenterContent).setOnClickListener(v -> {
            handler.removeCallbacks(launchRunnable);
            proceedToMain();
        });
    }

    private void proceedToMain() {
        Intent intent = new Intent(SplashActivity.this, MainActivity.class);
        startActivity(intent);
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (launchRunnable != null) {
            handler.removeCallbacks(launchRunnable);
        }
    }
}
