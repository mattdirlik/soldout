package com.example.soldout.model.tokenaccounts

import kotlinx.serialization.Serializable

@Serializable
data class TokenAccount(
    val address: String,
    val amount: Long,
    val delegated_amount: Int? = null,
    val frozen: Boolean? = null,
    val mint: String? = null,
    val owner: String? = null
)