package com.n2q.sample.billing

import com.n2q.sdk.billing.BillingConfig

class IBillingConfig: BillingConfig {

    override fun skus(): ArrayList<String> {
        return ArrayList()
    }

    override fun skuType() = BillingConfig.SkuType.IN_APP

}