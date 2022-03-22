package com.n2q.sdk.billing.callback

import com.android.billingclient.api.Purchase

interface PurchaseCallback {
    fun onSuccess(purchases: ArrayList<Purchase>)
    fun onUserCanceled()
    fun onError()
}