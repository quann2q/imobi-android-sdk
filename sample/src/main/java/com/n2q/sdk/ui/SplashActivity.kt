package com.n2q.sdk.ui

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.android.billingclient.api.SkuDetails
import com.n2q.sdk.billing.MobileBilling
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
        MobileBilling.verifyPurchase(object : MobileBilling.VerifyPurchaseCallback{
            override fun onPurchase(isPurchased: Boolean) {
                Log.e(TAG, "onPurchase: $isPurchased", )
            }

        })


    }

}