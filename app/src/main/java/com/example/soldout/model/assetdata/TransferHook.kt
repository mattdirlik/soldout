package com.example.soldout.model.assetdata

import kotlinx.serialization.Serializable

@Serializable
data class TransferHook(
    val authority: String? = null,
    val program_id: String? = null
)