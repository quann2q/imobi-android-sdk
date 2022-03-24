package com.n2q.sdk.billing

interface BillingConfig {
    fun skus(): ArrayList<String>
    fun skuType(): String
}