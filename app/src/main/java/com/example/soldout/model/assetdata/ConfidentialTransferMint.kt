package com.example.soldout.model.assetdata

import kotlinx.serialization.Serializable

@Serializable
data class ConfidentialTransferMint(
    val auditor_elgamal_pubkey: String,
    val authority: String,
    val auto_approve_new_accounts: Boolean
)