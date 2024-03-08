package com.example.soldout.model.assetdata

import kotlinx.serialization.Serializable

@Serializable
data class TransferFeeConfig(
    val newer_transfer_fee: NewerTransferFee? = null,
    val older_transfer_fee: OlderTransferFee? = null,
    val transfer_fee_config_authority: String? = null,
    val withdraw_withheld_authority: String? = null,
    val withheld_amount: Int? = null
)