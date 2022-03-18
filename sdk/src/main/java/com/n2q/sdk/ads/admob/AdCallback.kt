package com.n2q.sdk.ads.admob

import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.appopen.AppOpenAd
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.nativead.NativeAd
import com.google.android.gms.ads.rewarded.RewardedAd

abstract class AdCallback {
    fun onLoadError() {}
}

abstract class AdAppOpenCallback : AdCallback() {
    fun onLoaded(appOpenAd: AppOpenAd) {}
}

abstract class AdInterstitialCallback : AdCallback() {
    fun onLoaded(interstitialAd: InterstitialAd) {}
}

abstract class AdBannerCallback : AdCallback() {
    fun onLoaded(adView: AdView) {}
}

abstract class AdNativeCallback : AdCallback() {
    fun onLoaded(nativeAd: NativeAd) {}
}

abstract class AdRewardedCallback : AdCallback() {
    fun onLoaded(rewardedAd: RewardedAd) {}
}