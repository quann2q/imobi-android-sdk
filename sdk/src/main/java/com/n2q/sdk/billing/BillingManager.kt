package com.n2q.sdk.billing

import android.app.Activity
import android.content.Context
import android.util.Log
import com.android.billingclient.api.*
import com.n2q.sdk.billing.MobileBilling.*

class BillingManager : PurchasesUpdatedListener {

    private lateinit var mBillingConfig: BillingConfig

    private lateinit var mBillingClient: BillingClient
    private val mSkuDetails = ArrayList<SkuDetails>()

    private var mPurchaseCallback: PurchaseCallback? = null

    fun initBilling(context: Context, billingConfig: Class<out BillingConfig>) {
        mBillingConfig = billingConfig.newInstance()

        mBillingClient = BillingClient.newBuilder(context)
            .enablePendingPurchases()
            .setListener(this)
            .build()
    }

    private fun BillingConfig.SkuType.type() = if (this == BillingConfig.SkuType.SUBS) BillingClient.SkuType.SUBS else BillingClient.SkuType.INAPP

    fun launchBilling(activity: Activity, skuDetails: SkuDetails, purchaseCallback: PurchaseCallback) {
        mPurchaseCallback = purchaseCallback
        val billingFlowParams = BillingFlowParams.newBuilder().setSkuDetails(skuDetails).build()
        mBillingClient.launchBillingFlow(activity, billingFlowParams).responseCode
    }

    fun verifyPurchase(callback: VerifyPurchaseCallback) {
        if (mSkuDetails.isEmpty()) {
            connect(object : BillingQueryCallback {
                override fun onFinished(skuDetails: ArrayList<SkuDetails>?) {
                    skuDetails?.apply { verifyPurchase(callback) }
                }
            })
        }

        mBillingClient.queryPurchasesAsync(mBillingConfig.skuType().type()) { result, purchases ->
            if (result.responseCode != BillingClient.BillingResponseCode.OK) {
                callback.onPurchase(false)
                return@queryPurchasesAsync
            }

            purchases.forEach { purchase ->
                if (purchase.purchaseState == Purchase.PurchaseState.PURCHASED) {
                    callback.onPurchase(true)
                    return@queryPurchasesAsync
                }
                purchase.skus.forEach { sku ->
                    if (mBillingConfig.skus().contains(sku)) {
                        callback.onPurchase(true)
                        return@queryPurchasesAsync
                    }
                }
            }

            callback.onPurchase(false)
        }
    }

    fun connect(callback: BillingQueryCallback) {
        mBillingClient.startConnection(object : BillingClientStateListener {
            override fun onBillingServiceDisconnected() {
                connect(callback)
            }

            override fun onBillingSetupFinished(result: BillingResult) {
                if (result.responseCode == BillingClient.BillingResponseCode.OK) {
                    productDetails(callback)
                } else {
                    callback.onFinished(null)
                }
            }

        })
    }

    private fun productDetails(callback: BillingQueryCallback) {
        val productDetailsQuery = SkuDetailsParams
            .newBuilder()
            .setSkusList(mBillingConfig.skus())
            .setType(mBillingConfig.skuType().type())
            .build()

        mBillingClient.querySkuDetailsAsync(productDetailsQuery) { result, skus ->
            skus?.apply {
                if (result.responseCode == BillingClient.BillingResponseCode.OK) {
                    mSkuDetails.clear()
                    mSkuDetails.addAll(skus)
                }
                callback.onFinished(mSkuDetails)
                return@querySkuDetailsAsync
            }
            callback.onFinished(null)
        }
    }


    override fun onPurchasesUpdated(result: BillingResult, purchases: MutableList<Purchase>?) {
        purchases?.apply {
            when (result.responseCode) {
                BillingClient.BillingResponseCode.OK -> {
                    val mPurchases = ArrayList<Purchase>()
                    this.forEach { purchase ->
                        handlePurchase(purchase)
                        if (purchase.purchaseState == Purchase.PurchaseState.PURCHASED && !purchase.isAcknowledged) {
                            mPurchases.add(purchase)
                        }
                    }
                    mPurchaseCallback?.onSuccess(mPurchases)
                }
                BillingClient.BillingResponseCode.USER_CANCELED -> {
                    mPurchaseCallback?.onUserCanceled()
                }
                else -> {
                    mPurchaseCallback?.onError()
                }
            }
        }
    }

    private fun handlePurchase(purchase: Purchase) {
        val consumeParams = ConsumeParams.newBuilder().setPurchaseToken(purchase.purchaseToken).build()
        val listener = ConsumeResponseListener { _, _ ->
            // Handle the success of the consume operation.
        }
        mBillingClient.consumeAsync(consumeParams, listener)

        if (purchase.purchaseState == Purchase.PurchaseState.PURCHASED) {
            if (!purchase.isAcknowledged) {
                val params = AcknowledgePurchaseParams.newBuilder().setPurchaseToken(purchase.purchaseToken).build()
                mBillingClient.acknowledgePurchase(params) { result ->
                    Log.e("onAcknowledge", "result = $result")
                }
            }
        }
    }

}