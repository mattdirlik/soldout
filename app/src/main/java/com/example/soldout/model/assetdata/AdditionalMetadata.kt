package com.example.soldout.model.assetdata

import kotlinx.serialization.Serializable

@Serializable
data class AdditionalMetadata(
    val key: String,
    val value: String
)