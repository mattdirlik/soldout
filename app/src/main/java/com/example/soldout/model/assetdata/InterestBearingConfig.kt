package com.example.soldout.model.assetdata

import kotlinx.serialization.Serializable

@Serializable
data class InterestBearingConfig(
    val current_rate: Int,
    val initialization_timestamp: Int,
    val last_update_timestamp: Int,
    val pre_update_average_rate: Int,
    val rate_authority: String
)