package com.example.soldout.model.assetdata

import kotlinx.serialization.Serializable

@Serializable
data class Authorities(
    val address: String,
    val scopes: List<String>
)