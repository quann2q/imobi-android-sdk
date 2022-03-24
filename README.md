# iMobi SDK for Android

### 1. What does the SDK do?

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


### 2. Add SDK to App

#### 2.1. module -> build.gradle
```
   // Admob
   implementation 'com.google.android.gms:play-services-ads:20.6.0'
   
```

### 3. How to use
#### 3.1. Admob
    Call ```MobileAds.enableTest(true)``` to Class<out Application> to activate test ad