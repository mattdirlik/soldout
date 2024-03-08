package com.example.soldout.model.assetdata

import kotlinx.serialization.Serializable

@Serializable
data class Grouping(
    val group_key: String,
    val group_value: String
)