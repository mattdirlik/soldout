package com.example.soldout.model.assetdata

import kotlinx.serialization.Serializable

@Serializable
data class MetadataX(
    val additional_metadata: List<AdditionalMetadata>? = null,
    val mint: String? = null,
    val name: String? = null,
    val symbol: String? = null,
    val update_authority: String? = null,
    val uri: String? = null
)