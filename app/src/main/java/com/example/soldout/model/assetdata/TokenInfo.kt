package com.example.soldout.model.assetdata

import kotlinx.serialization.Serializable

@Serializable
data class TokenInfo(
    val decimals: Int? = null,
    val price_info: PriceInfo? = null,
    val supply: Int? = null,
    val symbol: String? = null,
    val token_program: String? = null
)