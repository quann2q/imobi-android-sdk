package com.n2q.sdk.billing

import android.app.Activity
import android.content.Context
import com.android.billingclient.api.Purchase
import com.android.billingclient.api.SkuDetails

class MobileBilling {

    companion object {
        private val mBillingManager = BillingManager()

        fun initBilling(context: Context, billingConfig: Class<out BillingConfig>){
            mBillingManager.initBilling(context, billingConfig)
        }

        fun launchBilling(activity: Activity, skuDetails: SkuDetails, purchaseCallback: PurchaseCallback){
            mBillingManager.launchBilling(activity, skuDetails, purchaseCallback)
        }

        fun verifyPurchase(callback: VerifyPurchaseCallback){
            mBillingManager.verifyPurchase(callback)
        }

        fun queryDetails(callback: BillingQueryCallback){
            mBillingManager.connect(callback)
        }

    }

    interface VerifyPurchaseCallback {
        fun onPurchase(isPurchased: Boolean)
    }

    interface BillingQueryCallback {
        fun onFinished(skuDetails: ArrayList<SkuDetails>)
    }

    interface PurchaseCallback {
        fun onSuccess(purchases: ArrayList<Purchase>)
        fun onUserCanceled()
        fun onError()
    }

}