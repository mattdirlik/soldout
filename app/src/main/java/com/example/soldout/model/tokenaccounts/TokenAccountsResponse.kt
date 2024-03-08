package com.example.soldout.model.tokenaccounts

import kotlinx.serialization.Serializable

@Serializable
data class TokenAccountsResponse(
    val limit: Int? = null,
    val page: Int? = null,
    val token_accounts: List<TokenAccount>? = null,
    val total: Int? = null
)