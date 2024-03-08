package com.example.soldout.model.assetdata

import kotlinx.serialization.Serializable

@Serializable
data class OlderTransferFee(
    val epoch: String,
    val maximum_fee: String,
    val transfer_fee_basis_points: String
)