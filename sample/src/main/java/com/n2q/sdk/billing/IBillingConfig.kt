package com.n2q.sdk.billing

class IBillingConfig: BillingConfig {

    override fun skus(): ArrayList<String> {
        return ArrayList()
    }

    override fun skuType() = BillingConfig.SkuType.IN_APP

}