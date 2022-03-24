package com.n2q.sdk

import android.app.Application
import com.n2q.sdk.billing.IBillingConfig
import com.n2q.sdk.billing.MobileBilling

class MainApp : Application() {

    override fun onCreate() {
        super.onCreate()

        MobileBilling.initBilling(this, IBillingConfig::class.java)

    }

}