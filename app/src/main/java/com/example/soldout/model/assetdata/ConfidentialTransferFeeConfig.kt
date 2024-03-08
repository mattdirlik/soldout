package com.example.soldout.model.assetdata

import kotlinx.serialization.Serializable

@Serializable
data class ConfidentialTransferFeeConfig(
    val authority: String,
    val harvest_to_mint_enabled: Boolean,
    val withdraw_withheld_authority_elgamal_pubkey: String,
    val withheld_amount: String
)