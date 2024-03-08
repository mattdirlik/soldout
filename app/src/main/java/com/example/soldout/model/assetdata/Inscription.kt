package com.example.soldout.model.assetdata

import kotlinx.serialization.Serializable

@Serializable
data class Inscription(
    val authority: String,
    val contentType: String,
    val encoding: String,
    val inscriptionDataAccount: String,
    val order: Int,
    val size: Int,
    val validationHash: String
)