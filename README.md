# Orche’ — Luxury Intimates & Omnichannel Retail Experience

<p align="center">
  <img src="app/src/main/res/drawable/logo_orche_full_crop.png" alt="Orche' Logo" width="220" />
</p>

<p align="center">
  <b>Thoughtful Intimates, Crafted for Everyday Ease</b><br>
  <i>An omnichannel mobile e-commerce platform bridging online shopping with boutique in-store digital pickup and fitting experiences.</i>
</p>

<p align="center">
  <img src="https://img.shields.io/badge/Platform-Android-brightgreen.svg" alt="Platform" />
  <img src="https://img.shields.io/badge/Language-Java-orange.svg" alt="Language" />
  <img src="https://img.shields.io/badge/UI-Material%20Design%203-blue.svg" alt="Material 3" />
  <img src="https://img.shields.io/badge/Architecture-Fragment%20SPA-purple.svg" alt="Architecture" />
  <img src="https://img.shields.io/badge/Min%20SDK-24-informational.svg" alt="Min SDK" />
</p>

---

## 📖 Overview

**Orche’** is a production-grade, native Android shopping application designed specifically for luxury women's innerwear, loungewear, and intimates. Built with an omnichannel retail architecture inspired by retail leaders (e.g. McDonald's app digital passes and boutique retail pickup), it unifies online home delivery with smart in-store boutique services.

---

## ✨ Key Features (12-Module Launch Checklist)

### 🛍️ 1. Catalog, Search, Filter & Multi-Criteria Sort
- Instant real-time search across categories (Bras, Briefs, Sleepwear, Sets).
- Dynamic modal filter sheet by maximum price range, size availability, color swatches, and multi-criteria sorting (Price Low to High, Price High to Low, Customer Rating).

### 📐 2. Size Guide & Interactive Fit Finder
- Measurement guide explaining how to measure band and bust accurately at home.
- Real-time interactive **Fit Finder Calculator** with sister-size recommendation engine (e.g., 34B with sister fits 32C and 36A).
- International conversion matrix (US/UK, EU, FR) and fit troubleshooting FAQs.

### 📍 3. Address & Delivery Availability Validator
- GPS location detection using **Android Runtime Location Permission** to identify the nearest boutique and PIN code automatically.
- Live 6-digit PIN code delivery availability checker calculating express same-day and standard 2-day delivery routes.

### 🏬 4. Omnichannel In-Store Experience
- **In-Store Barcode Scanner** mockup using **Camera / Media Permissions** to scan garment barcodes directly off physical boutique racks.
- Instant routing to send garments directly to fitting rooms or reserve for express boutique counter pickup.
- **Digital Store Pass / Barcode**: McDonald's-style barcode pass generated with order confirmations for seamless store pickup verification.

### 💳 5. Payment Selection & Safe Retry Path
- Multi-channel payment simulation: UPI (GPay, PhonePe, Paytm), Credit/Debit cards, Net Banking, and Cash on Delivery.
- Realistic **Payment Result & Failure Retry Gateway**: graceful declined payment simulation with bag preservation and 1-click safe retry paths.

### 📦 6. Order Tracking & Confirmation
- Step-by-step vertical progress tracking (Confirmed, Packed, Out for Delivery, Boutique Ready).
- Live OTP generator for secure courier delivery handoffs.

### 🌿 7. Returns & Hygiene Policy
- Clear, category-specific eligibility cards (hygiene-seal rules for briefs vs. 15-day exchanges for bras and loungewear).
- Interactive 1-click return/exchange request flow with order selection.

### 💬 8. Concierge & Support with Android Intents
- Native Android **Intents** for direct customer care:
  - Phone call intent (`tel:`) to call concierge.
  - Email draft intent (`mailto:`) for support tickets.
  - Geo-map intent (`geo:`) with turn-by-turn directions to the flagship Bandra boutique.

### 👑 9. Offers, Loyalty & Orche' Circle
- Tiered member loyalty program (*Orche' Circle Gold*) with real-time point balance tracking and bag redemption.
- Promo coupon manager with one-tap application.

### ⭐ 10. Verified Customer Reviews & Fit Feedback
- Rating breakdown (4.8 ★ average) and fit accuracy metrics (Runs Small, True to Size, Runs Large).
- Review submission modal with **Camera & Photo Gallery Picker** integration for fit photos.

### 🔔 11. Notifications Inbox
- Order dispatch alerts, boutique fitting ready updates, and exclusive Circle promotions.

### 👤 12. Account, Privacy & Discreet Billing
- Guest checkout + member login modal.
- Discreet packaging and billing statement toggles (`APPAREL RETAIL` billing descriptor).
- Account management and data deletion compliance options.

---

## 🎨 Design & Typography System

Orche’ employs a bespoke three-tier luxury typography system:
- **Brandmark / Logo**: Vintage-Inspired Serif font family (`font_orche_brand`).
- **Product Names**: Segoe UI (`font_product_name`) for modern clean legibility.
- **Action Buttons & Headings**: Cormorant Garamond (`font_cormorant`) configured as the default theme typography.
- **Palette**: Velvet Plum (`#54253B`), Silk Ivory (`#FAF5F1` / `#FBF3ED`), Rose Accent (`#D892A2`), and Amber Gold (`#C99700`).

---

## 🛠️ Tech Stack & Architecture

- **Language**: Java 17 / Android SDK
- **Architecture**: Single-Activity Multiple-Fragment Architecture (`MainActivity`, `SplashActivity`)
- **State Management**: Singleton In-Memory Repository Pattern (`AppState`)
- **Permissions Handled**: `ACCESS_FINE_LOCATION`, `ACCESS_COARSE_LOCATION`, `CAMERA`, `READ_MEDIA_IMAGES`
- **Intents**: `ACTION_DIAL`, `ACTION_SENDTO`, `ACTION_VIEW` (Google Maps)
- **UI Components**: Material Design 3 (`MaterialButton`, `MaterialCardView`, BottomNavigationView)

---

## 🚀 Getting Started

### Prerequisites
- Android Studio Ladybug (or newer)
- Android SDK Platform 34+
- Java Development Kit (JDK 17+)

### Building from Source

```bash
# 1. Clone the repository
git clone https://github.com/<your-username>/orche.git
cd orche

# 2. Build debug APK
./gradlew assembleDebug

# Output APK will be at:
# app/build/outputs/apk/debug/app-debug.apk
```

---

## 📱 App Icon & Splash Screen

- **Launcher Icon**: Adaptive icon with custom woman portrait emblem and warm ivory background.
- **Splash Screen**: Animated logo reveal with smooth crossfade into the store experience.
