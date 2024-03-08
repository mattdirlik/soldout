package com.example.soldout.model.assetdata

import kotlinx.serialization.Serializable

@Serializable
data class MetadataPointer(
    val authority: String? = null,
    val metadata_address: String? = null
)