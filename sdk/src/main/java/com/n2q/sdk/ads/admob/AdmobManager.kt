package com.n2q.sdk.ads.admob

import android.app.Activity
import com.google.android.gms.ads.*
import com.google.android.gms.ads.appopen.AppOpenAd
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback
import com.google.android.gms.ads.rewarded.RewardedAd
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback
import com.n2q.sdk.billing.BillingManager
import com.n2q.sdk.billing.callback.VerifyPurchaseCallback

class AdmobManager {

    companion object {
        private const val TAG = "AdmobManager"

        private var mInstance: AdmobManager? = null

        fun instance(): AdmobManager {
            if (mInstance == null) {
                mInstance = AdmobManager()
            }

            return mInstance as AdmobManager
        }

    }

    private val mAdRequest = AdRequest.Builder().build()

    private var mAppOpenAd: AppOpenAd? = null
    private var mMapInterstitialAd = HashMap<String, InterstitialAd?>()
    private var mMapBannerAd = HashMap<String, AdView?>()
    private var mMapRewardedAd = HashMap<String, RewardedAd?>()

    /**
     * LOAD AD
     * @param callback extended AdCallback
     *          AdAppOpenCallback       for load App Open
     *          AdInterstitialCallback  for load Interstitial
     *          AdBannerCallback        for load Banner
     *          AdNativeCallback        for load Native
     *          AdRewardedCallback      for load Reward
     */
    fun loadAd(activity: Activity, idAd: String, adFormat: AdFormat, callback: AdCallback? = null) {
        BillingManager.instance().verifyPurchase(object : VerifyPurchaseCallback {
            override fun onPurchase(isPurchased: Boolean) {
                if (isPurchased) return
                activity.runOnUiThread {

                    when (adFormat) {
                        AdFormat.APP_OPEN ->
                            loadAppOpen(activity, idAd, callback as? AdAppOpenCallback)
                        AdFormat.INTERSTITIAL ->
                            loadInterstitial(activity, idAd, callback as? AdInterstitialCallback)
                        AdFormat.BANNER ->
                            loadBanner(activity, idAd, callback as? AdBannerCallback)
                        AdFormat.NATIVE ->
                            loadNative(activity, idAd, callback as? AdNativeCallback)
                        AdFormat.REWARDED ->
                            loadRewarded(activity, idAd, callback as? AdRewardedCallback)
                    }

                }
            }

        })

    }

    private fun loadAppOpen(activity: Activity, idAd: String, callback: AdAppOpenCallback? = null) {
        mAppOpenAd = null
        AppOpenAd.load(activity, idAd, mAdRequest, AppOpenAd.APP_OPEN_AD_ORIENTATION_PORTRAIT, object : AppOpenAd.AppOpenAdLoadCallback() {

            override fun onAdLoaded(appOpenAd: AppOpenAd) {
                mAppOpenAd = appOpenAd
                callback?.onLoaded(appOpenAd)
            }

            override fun onAdFailedToLoad(error: LoadAdError) {
                callback?.onLoadError(error)
            }

        })
    }

    private fun loadInterstitial(activity: Activity, idAd: String, callback: AdInterstitialCallback? = null) {
        mMapInterstitialAd[idAd] = null
        InterstitialAd.load(activity, idAd, mAdRequest, object : InterstitialAdLoadCallback() {

            override fun onAdLoaded(interstitialAd: InterstitialAd) {
                mMapInterstitialAd[idAd] = interstitialAd
                callback?.onLoaded(interstitialAd)
            }

            override fun onAdFailedToLoad(error: LoadAdError) {
                callback?.onLoadError(error)
            }

        })
    }

    private fun loadBanner(activity: Activity, idAd: String, callback: AdBannerCallback? = null) {
        mMapBannerAd[idAd] = null
        val adView = AdView(activity)
        adView.adSize = AdSize.BANNER
        adView.adUnitId = idAd

        // Internet access is required
        adView.loadAd(mAdRequest)

        adView.adListener = object : AdListener() {

            override fun onAdLoaded() {
                mMapBannerAd[idAd] = adView
                callback?.onLoaded(adView)
            }

        }
    }

    private fun loadNative(activity: Activity, idAd: String, callback: AdNativeCallback? = null) {
        AdLoader.Builder(activity, idAd)
            .forNativeAd { nativeAd -> callback?.onLoaded(nativeAd) }
            .withAdListener(object : AdListener() {
                override fun onAdFailedToLoad(error: LoadAdError) {
                    callback?.onLoadError(error)
                }
            })
            .build()
    }

    private fun loadRewarded(activity: Activity, idAd: String, callback: AdRewardedCallback? = null) {
        mMapRewardedAd[idAd] = null
        RewardedAd.load(activity, idAd, mAdRequest, object : RewardedAdLoadCallback() {
            override fun onAdLoaded(rewardedAd: RewardedAd) {
                mMapRewardedAd[idAd] = rewardedAd
                callback?.onLoaded(rewardedAd)
            }

            override fun onAdFailedToLoad(error: LoadAdError) {
                callback?.onLoadError(error)
            }
        })
    }


    /**
     * SHOW AD
     */

    fun showAppOpenAd(activity: Activity, idAd: String) {
        mAppOpenAd?.show(activity)

        loadAd(activity, idAd, AdFormat.APP_OPEN)
    }

    fun showAppOpenAd(activity: Activity, appOpenAd: AppOpenAd) {
        appOpenAd.show(activity)

        loadAd(activity, appOpenAd.adUnitId, AdFormat.APP_OPEN)
    }

    fun showInterstitialAd(activity: Activity, idAd: String) {
        mMapInterstitialAd[idAd]?.show(activity)

        loadAd(activity, idAd, AdFormat.INTERSTITIAL)
    }

    fun showInterstitialAd(activity: Activity, interstitialAd: InterstitialAd) {
        interstitialAd.show(activity)

        loadAd(activity, interstitialAd.adUnitId, AdFormat.INTERSTITIAL)
    }

    fun showBannerAd(activity: Activity, idAd: String, callback: AdBannerShowCallback) {
        mMapBannerAd[idAd]?.apply {
            callback.onReady(this)
        }

        loadAd(activity, idAd, AdFormat.BANNER)
    }

    fun showRewardedAd(activity: Activity, idAd: String, callback: OnUserEarnedRewardListener) {
        mMapRewardedAd[idAd]?.show(activity, callback)

        loadAd(activity, idAd, AdFormat.REWARDED)
    }

    fun showRewardedAd(activity: Activity, rewardedAd: RewardedAd, callback: OnUserEarnedRewardListener) {
        rewardedAd.show(activity, callback)

        loadAd(activity, rewardedAd.adUnitId, AdFormat.REWARDED)
    }


}