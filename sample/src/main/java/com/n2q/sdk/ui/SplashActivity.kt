package com.n2q.sdk.ui

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.OnUserEarnedRewardListener
import com.google.android.gms.ads.appopen.AppOpenAd
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.rewarded.RewardItem
import com.google.android.gms.ads.rewarded.RewardedAd
import com.n2q.sdk.R
import com.n2q.sdk.ads.admob.*
import com.n2q.sdk.databinding.ActivitySplashBinding

class SplashActivity : AppCompatActivity() {

    companion object {
        private const val TAG = "SplashActivity"
    }

    private lateinit var binding: ActivitySplashBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)

        adProcess()

        binding.btnNext.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
        }
    }

    private fun adProcess() {
        val idBanner = getString(R.string.banner_test)
        AdmobManager.instance().loadAd(this, idBanner, AdFormat.BANNER)

        Handler(Looper.getMainLooper()).postDelayed({
            AdmobManager.instance().showBannerAd(this, idBanner, object : AdBannerShowCallback {
                override fun onReady(adView: AdView) {
                    binding.layoutAdBanner.removeAllViews()
                    binding.layoutAdBanner.addView(adView)
                }
            })
        }, 5000)
    }

}