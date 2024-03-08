package com.example.soldout.model.assetdata

import kotlinx.serialization.Serializable

@Serializable
data class Compression(
    val asset_hash: String,
    val compressed: Boolean,
    val creator_hash: String,
    val data_hash: String,
    val eligible: Boolean,
    val leaf_id: Int,
    val seq: Int,
    val tree: String
)