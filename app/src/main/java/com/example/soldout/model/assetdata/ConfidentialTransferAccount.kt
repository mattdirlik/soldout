package com.example.soldout.model.assetdata

import kotlinx.serialization.Serializable

@Serializable
data class ConfidentialTransferAccount(
    val actual_pending_balance_credit_counter: Int,
    val allow_confidential_credits: Boolean,
    val allow_non_confidential_credits: Boolean,
    val approved: Boolean,
    val available_balance: String,
    val decryptable_available_balance: String,
    val elgamal_pubkey: String,
    val expected_pending_balance_credit_counter: Int,
    val maximum_pending_balance_credit_counter: Int,
    val pending_balance_credit_counter: Int,
    val pending_balance_hi: String,
    val pending_balance_lo: String
)