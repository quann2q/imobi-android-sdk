package com.n2q.sdk.billing

interface BillingConfig {
    fun skus(): ArrayList<String>
    fun skuType(): SkuType

    enum class SkuType {IN_APP, SUBS}

}