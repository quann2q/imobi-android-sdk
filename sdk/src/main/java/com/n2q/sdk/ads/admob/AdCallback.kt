package com.n2q.sdk.ads.admob

import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.appopen.AppOpenAd
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.nativead.NativeAd
import com.google.android.gms.ads.rewarded.RewardedAd

abstract class AdCallback {
    open fun onLoadError(error: LoadAdError) {}
}

abstract class AdAppOpenCallback : AdCallback() {
    open fun onLoaded(appOpenAd: AppOpenAd) {}
}

abstract class AdInterstitialCallback : AdCallback() {
    open fun onLoaded(interstitialAd: InterstitialAd) {}
}

abstract class AdBannerCallback : AdCallback() {
    open fun onLoaded(adView: AdView) {}
}

interface AdBannerShowCallback {
    fun onReady(adView: AdView) {}
}

abstract class AdNativeCallback : AdCallback() {
    open fun onLoaded(nativeAd: NativeAd) {}
}

abstract class AdRewardedCallback : AdCallback() {
    open fun onLoaded(rewardedAd: RewardedAd) {}
}