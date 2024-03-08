package com.example.soldout.model.assetdata

import kotlinx.serialization.Serializable

@Serializable
data class Metadata(
    val attributes: List<Attribute>? = null,
    val description: String? = null,
    val name: String,
    val symbol: String
)