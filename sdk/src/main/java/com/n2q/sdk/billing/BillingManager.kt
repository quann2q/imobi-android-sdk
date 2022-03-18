package com.n2q.sdk.billing

import android.content.Context
import com.android.billingclient.api.BillingClient
import com.android.billingclient.api.BillingResult
import com.android.billingclient.api.Purchase
import com.android.billingclient.api.PurchasesUpdatedListener
import com.google.android.gms.ads.AdRequest

class BillingManager(context: Context) : PurchasesUpdatedListener {

    override fun onPurchasesUpdated(billingResult: BillingResult, purchases: MutableList<Purchase>?) {

    }

    private var mBillingClient: BillingClient =
        BillingClient.newBuilder(context)
            .enablePendingPurchases()
            .setListener(this)
            .build()


    companion object {
        fun isPurchased() = false
    }

}