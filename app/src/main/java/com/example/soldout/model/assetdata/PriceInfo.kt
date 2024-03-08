package com.example.soldout.model.assetdata

import kotlinx.serialization.Serializable

@Serializable
data class PriceInfo(
    val currency: String? = null,
    val price_per_token: Int? = null
)