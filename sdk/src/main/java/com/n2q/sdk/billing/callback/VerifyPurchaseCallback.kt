package com.n2q.sdk.billing.callback

interface VerifyPurchaseCallback {
    fun onPurchase(isPurchased: Boolean)
}