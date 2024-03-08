package com.example.soldout.model.assetdata

import kotlinx.serialization.Serializable

@Serializable
data class MintCloseAuthority(
    val close_authority: String? = null
)