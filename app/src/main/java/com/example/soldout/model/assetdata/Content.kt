package com.example.soldout.model.assetdata

import kotlinx.serialization.Serializable

@Serializable
data class Content(
    val `$schema`: String,
    val files: List<File>? = null,
    val items: Items? = null,
    val json_uri: String,
    val links: Links? = null,
    val metadata: Metadata
)