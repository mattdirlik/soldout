package com.example.soldout.model.assetdata

import kotlinx.serialization.Serializable

@Serializable
data class Creators(
    val address: String,
    val share: Int,
    val verified: Boolean
)