package com.n2q.sdk.ads.admob

import android.app.Activity
import com.google.android.gms.ads.*
import com.google.android.gms.ads.appopen.AppOpenAd
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback
import com.google.android.gms.ads.nativead.NativeAd
import com.google.android.gms.ads.rewarded.RewardedAd
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback
import com.n2q.sdk.ads.admob.MobileAds.AdCallback

class AdmobManager {

    private val mAdRequest = AdRequest.Builder().build()

    private var mAppOpenAd: AppOpenAd? = null
    private var mMapBannerAd = HashMap<String, AdView?>()
    private var mMapInterstitialAd = HashMap<String, InterstitialAd?>()
    private var mMapNativeAd = HashMap<String, NativeAd?>()
    private var mMapRewardedAd = HashMap<String, RewardedAd?>()

    /**
     * LOAD AD
     */
    fun loadAd(activity: Activity, adFormat: AdFormat, idAd: String = "", callback: AdCallback? = null) {

        when (adFormat) {
            AdFormat.APP_OPEN ->
                loadAppOpen(activity, idAd, callback)
            AdFormat.INTERSTITIAL ->
                loadInterstitial(activity, idAd, callback)
            AdFormat.BANNER ->
                loadBanner(activity, idAd, callback)
            AdFormat.NATIVE ->
                loadNative(activity, idAd, callback)
            AdFormat.REWARDED ->
                loadRewarded(activity, idAd, callback)
        }

    }

    private fun loadAppOpen(activity: Activity, idAd: String, callback: AdCallback? = null) {
        mAppOpenAd = null
        AppOpenAd.load(activity, idAd, mAdRequest, AppOpenAd.APP_OPEN_AD_ORIENTATION_PORTRAIT, object : AppOpenAd.AppOpenAdLoadCallback() {

            override fun onAdLoaded(appOpenAd: AppOpenAd) {
                mAppOpenAd = appOpenAd
                callback?.onLoaded()
            }

            override fun onAdFailedToLoad(error: LoadAdError) {
                callback?.onLoadError(error)
            }

        })
    }

    private fun loadInterstitial(activity: Activity, idAd: String, callback: AdCallback? = null) {
        mMapInterstitialAd[idAd] = null
        InterstitialAd.load(activity, idAd, mAdRequest, object : InterstitialAdLoadCallback() {

            override fun onAdLoaded(interstitialAd: InterstitialAd) {
                mMapInterstitialAd[idAd] = interstitialAd
                callback?.onLoaded()
            }

            override fun onAdFailedToLoad(error: LoadAdError) {
                callback?.onLoadError(error)
            }

        })
    }

    private fun loadBanner(activity: Activity, idAd: String, callback: AdCallback? = null) {
        mMapBannerAd[idAd] = null
        val adView = AdView(activity)
        adView.adSize = AdSize.BANNER
        adView.adUnitId = idAd

        // Internet access is required
        adView.loadAd(mAdRequest)

        adView.adListener = object : AdListener() {

            override fun onAdLoaded() {
                mMapBannerAd[idAd] = adView
                callback?.onLoaded()
            }

        }
    }

    private fun loadNative(activity: Activity, idAd: String, callback: AdCallback? = null) {
        mMapNativeAd[idAd] = null
        AdLoader.Builder(activity, idAd)
            .forNativeAd { nativeAd ->
                mMapNativeAd[idAd] = nativeAd
                callback?.onLoaded()
            }
            .withAdListener(object : AdListener() {
                override fun onAdFailedToLoad(error: LoadAdError) {
                    callback?.onLoadError(error)
                }
            })
            .build()
    }

    private fun loadRewarded(activity: Activity, idAd: String, callback: AdCallback? = null) {
        mMapRewardedAd[idAd] = null
        RewardedAd.load(activity, idAd, mAdRequest, object : RewardedAdLoadCallback() {
            override fun onAdLoaded(rewardedAd: RewardedAd) {
                mMapRewardedAd[idAd] = rewardedAd
                callback?.onLoaded()
            }

            override fun onAdFailedToLoad(error: LoadAdError) {
                callback?.onLoadError(error)
            }
        })
    }


    /**
     * SHOW AD
     */

    fun showAppOpenAd(activity: Activity) {
        mAppOpenAd?.apply {
            show(activity)
            loadAd(activity, AdFormat.APP_OPEN, this.adUnitId)
        }
    }

    fun showInterstitialAd(activity: Activity, idAd: String) {
        showInterstitialAd(activity, mMapInterstitialAd[idAd])
    }

    private fun showInterstitialAd(activity: Activity, interstitialAd: InterstitialAd?) {
        interstitialAd?.apply {
            show(activity)
            loadAd(activity, AdFormat.INTERSTITIAL, this.adUnitId)
        }
    }

    fun bannerAd(activity: Activity, idAd: String): AdView? {
        mMapBannerAd[idAd]?.apply {
            loadAd(activity, AdFormat.BANNER, idAd)
            return this
        }
        return null
    }

    fun nativeAd(activity: Activity, idAd: String): NativeAd? {
        mMapNativeAd[idAd]?.apply {
            loadAd(activity, AdFormat.NATIVE, idAd)
            return this
        }

        return null
    }

    fun showRewardedAd(activity: Activity, idAd: String, callback: OnUserEarnedRewardListener) {
        showRewardedAd(activity, mMapRewardedAd[idAd], callback)
    }

    private fun showRewardedAd(activity: Activity, rewardedAd: RewardedAd?, callback: OnUserEarnedRewardListener) {
        rewardedAd?.apply {
            show(activity, callback)
            loadAd(activity, AdFormat.REWARDED, this.adUnitId)
        }
    }


}