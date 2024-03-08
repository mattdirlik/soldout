package com.example.soldout.model.assetdata

import kotlinx.serialization.Serializable

@Serializable
data class Ownership(
    val burnt: Boolean? = null,
    val `delegate`: String? = null,
    val delegated: Boolean? = null,
    val frozen: Boolean? = null,
    val mutable: Boolean? = null,
    val owner: String? = null,
    val ownership_model: String? = null,
    val supply: String? = null
)