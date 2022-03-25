# iMobi SDK for Android

## 1. What does the SDK do?

 - AdNetwork
    + Admob
    + FAN

 - Billing (Google Service)
    + In-app purchase (Type INAPP)
    + Subscription (Type SUBS)
   
 - Firebase Tracking
    + FirebaseAnalytics

 - Release App with Shell Scrip
    + Shell scrip code
   
- Encode
    + ZKM
    + Proguard


## 2. Add SDK to App

#### 2.1. module -> build.gradle
```
   // Admob
   implementation 'com.google.android.gms:play-services-ads:20.6.0'
   
   // Google Billing
   implementation "com.android.billingclient:billing:4.1.0"
```


## 3. How to use

#### 3.1. Admob
Add test ad (Call in ``Class<out Application>``)
```
   MobileAds.enableTest(isEnable: Boolean)
```

Load AD Test
```
   MobileAds.loadAd(activity: Activity, adFormat: AdFormat, callback: AdCallback? = null)
```

Load AD
```
   MobileAds.loadAd(activity: Activity, adFormat: AdFormat, idAd: String, callback: AdCallback? = null)
```

Show AD
```
   MobileAds.showAppOpenAd(activity: Activity)
   MobileAds.showInterstitialAd(activity: Activity, idAd: String)
   MobileAds.showRewardedAd(activity: Activity, idAd: String, callback: OnUserEarnedRewardListener)
   MobileAds.bannerAd(activity: Activity, idAd: String) #return AdView
   MobileAds.nativeAd(activity: Activity, idAd: String) #return NativeAd
```

#### 3.2. Billing
Init Billing (Call in ``Class<out Application>``) with params "billingConfig" is class extended BillingConfig
```
   MobileBilling.initBilling(context: Context, billingConfig: Class<out BillingConfig>)
```

Get products available to buy
```
   MobileBilling.queryDetails(callback: BillingQueryCallback)
```

Launch the purchase flow
```
   MobileBilling.launchBilling(activity: Activity, skuDetails: SkuDetails, purchaseCallback: PurchaseCallback)
```

Check purchased
```
   MobileBilling.verifyPurchase(callback: VerifyPurchaseCallback)
```

#### 3.3. Firebase
 - Add the Firebase Android configuration file to your app
    + Download ``google-services.json`` obtain your Firebase Android config file
    + Move your config file into the module (app-level) directory of your app
 
- Enable Firebase products in your app
    + Root-level (project-level) Gradle file (``build.gradle``)
    ```
      classpath 'com.google.gms:google-services:4.3.10'
    ```
   
    + In your module (app-level) Gradle file (usually ``app/build.gradle``)
    ```
      apply plugin: 'com.google.gms.google-services'
      --------- OR
      plugins {
         id 'com.google.gms.google-services'
      }
    ```

 - Add Firebase SDKs to your app (without ``-ktx`` with Java)
    ```
      // Firebase
      implementation platform('com.google.firebase:firebase-bom:29.0.3')
      implementation 'com.google.firebase:firebase-crashlytics-ktx'
      implementation 'com.google.firebase:firebase-analytics-ktx'
      implementation 'com.google.firebase:firebase-auth-ktx'
      implementation 'com.google.firebase:firebase-firestore-ktx'
    ```
   
 - Use
    + Declare object at the top of your activity
    ```
      private lateinit var firebaseAnalytics: FirebaseAnalytics
    ```
    + Initialize it in the ``onCreate()`` method
    ```
      firebaseAnalytics = Firebase.analytics
    ```