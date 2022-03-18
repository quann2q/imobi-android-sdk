package com.n2q.sdk.ads.admob

import android.app.Activity
import com.google.android.gms.ads.*
import com.google.android.gms.ads.appopen.AppOpenAd
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback
import com.google.android.gms.ads.rewarded.RewardedAd
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback
import com.n2q.sdk.billing.BillingManager

class AdmobManager(private val activity: Activity) {

    private val mAdRequest = AdRequest.Builder().build()

    private var mAppOpenAd: AppOpenAd? = null
    private var mMapInterstitialAd = HashMap<String, InterstitialAd?>()
//    private var mMapBannerAd = HashMap<String, InterstitialAd?>()
//    private var mMapInterstitialAd = HashMap<String, InterstitialAd?>()
//    private var mMapInterstitialAd = HashMap<String, InterstitialAd?>()

    /**
     * LOAD AD
     */
    fun loadAd(idAd: String, adFormat: AdFormat, callback: AdCallback? = null) {

        if (BillingManager.isPurchased()) return

        when (adFormat) {
            AdFormat.APP_OPEN ->
                loadAppOpen(idAd, callback as AdAppOpenCallback)
            AdFormat.INTERSTITIAL ->
                loadInterstitial(idAd, callback as AdInterstitialCallback)
            AdFormat.BANNER ->
                loadBanner(idAd, callback as AdBannerCallback)
            AdFormat.NATIVE ->
                loadNative(idAd, callback as AdNativeCallback)
            AdFormat.REWARDED ->
                loadRewarded(idAd, callback as AdRewardedCallback)
        }

    }

    private fun loadAppOpen(idAd: String, callback: AdAppOpenCallback? = null) {
        mAppOpenAd = null
        AppOpenAd.load(activity, idAd, mAdRequest, AppOpenAd.APP_OPEN_AD_ORIENTATION_PORTRAIT, object : AppOpenAd.AppOpenAdLoadCallback() {

            override fun onAdLoaded(appOpenAd: AppOpenAd) {
                mAppOpenAd = appOpenAd
                callback?.onLoaded(appOpenAd)
            }

            override fun onAdFailedToLoad(error: LoadAdError) {
                callback?.onLoadError()
            }

        })
    }

    private fun loadInterstitial(idAd: String, callback: AdInterstitialCallback? = null) {
        mMapInterstitialAd[idAd] = null
        InterstitialAd.load(activity, idAd, mAdRequest, object : InterstitialAdLoadCallback() {

            override fun onAdLoaded(interstitialAd: InterstitialAd) {
                mMapInterstitialAd[idAd] = interstitialAd
                callback?.onLoaded(interstitialAd)
            }

            override fun onAdFailedToLoad(error: LoadAdError) {
                callback?.onLoadError()
            }

        })
    }

    private fun loadBanner(idAd: String, callback: AdBannerCallback? = null) {
        val adView = AdView(activity)
        adView.adSize = AdSize.BANNER
        adView.adUnitId = idAd

        // Internet access is required
        adView.loadAd(mAdRequest)

        adView.adListener = object : AdListener() {

            override fun onAdLoaded() {
                callback?.onLoaded(adView)
            }

        }
    }

    private fun loadNative(idAd: String, callback: AdNativeCallback? = null) {
        AdLoader.Builder(activity, idAd)
            .forNativeAd { nativeAd -> callback?.onLoaded(nativeAd) }
            .withAdListener(object : AdListener() {
                override fun onAdFailedToLoad(error: LoadAdError) {
                    callback?.onLoadError()
                }
            })
            .build()
    }

    private fun loadRewarded(idAd: String, callback: AdRewardedCallback? = null) {
        RewardedAd.load(activity, idAd, mAdRequest, object : RewardedAdLoadCallback() {
            override fun onAdLoaded(rewardedAd: RewardedAd) {
                callback?.onLoaded(rewardedAd)
            }

            override fun onAdFailedToLoad(error: LoadAdError) {

            }
        })
    }


    /**
     * SHOW AD
     */
    fun showAd(idAd: String, adFormat: AdFormat) {


        loadAd(idAd, adFormat)
    }

}