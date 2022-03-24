package com.n2q.sdk.ads.admob

import android.app.Activity
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.OnUserEarnedRewardListener

class MobileAds {

    companion object {

        private val admobManager = AdmobManager()

        /**
         * ID TEST
         */
        private const val APP_OPEN_TEST = "ca-app-pub-3940256099942544/3419835294"
        private const val BANNER_TEST = "ca-app-pub-3940256099942544/6300978111"
        private const val INTERSTITIAL_TEST = "ca-app-pub-3940256099942544/1033173712"
        private const val NATIVE_TEST = "ca-app-pub-3940256099942544/2247696110"
        private const val REWARD_TEST = "ca-app-pub-3940256099942544/5224354917"

        private var mEnableTest = false

        fun enableTest(isEnable: Boolean) {
            mEnableTest = isEnable
        }

        /**
         * LOAD AD
         */

        fun loadAd(activity: Activity, adFormat: AdFormat, idAd: String, callback: AdCallback? = null) {
            admobManager.loadAd(activity, adFormat, idAd, callback)
        }

        fun loadAdTest(activity: Activity, adFormat: AdFormat, callback: AdCallback? = null) {
            var idTest = ""
            if (mEnableTest) {
                idTest = when (adFormat) {
                    AdFormat.APP_OPEN -> APP_OPEN_TEST
                    AdFormat.INTERSTITIAL -> INTERSTITIAL_TEST
                    AdFormat.BANNER -> BANNER_TEST
                    AdFormat.NATIVE -> NATIVE_TEST
                    AdFormat.REWARDED -> REWARD_TEST
                }
            }
            loadAd(activity, adFormat, idTest, callback)
        }

        /**
         * SHOW AD
         */

        fun showAppOpenAd(activity: Activity) {
            admobManager.showAppOpenAd(activity)
        }

        fun showInterstitialAd(activity: Activity, idAd: String = "") {
            admobManager.showInterstitialAd(activity, if (mEnableTest) INTERSTITIAL_TEST else idAd)
        }

        fun showRewardedAd(activity: Activity, idAd: String = "", callback: OnUserEarnedRewardListener) {
            admobManager.showRewardedAd(activity, if (mEnableTest) REWARD_TEST else idAd, callback)
        }

        fun bannerAd(activity: Activity, idAd: String = "") =
            admobManager.bannerAd(activity, if (mEnableTest) BANNER_TEST else idAd)

        fun nativeAd(activity: Activity, idAd: String = "") =
            admobManager.nativeAd(activity, if (mEnableTest) NATIVE_TEST else idAd)

    }

    /**
     * CALL BACK
     */
    abstract class AdCallback {
        open fun onLoaded() {}
        open fun onLoadError(error: LoadAdError) {}
    }

}