package com.example.soldout.model.assetdata

import kotlinx.serialization.Serializable

@Serializable
data class Royalty(
    val basis_points: Int? = null,
    val locked: Boolean? = null,
    val percent: Int? = null,
    val primary_sale_happened: Boolean? = null,
    val royalty_model: String? = null,
    val target: String? = null
)