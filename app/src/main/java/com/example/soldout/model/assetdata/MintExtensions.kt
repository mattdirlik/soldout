package com.example.soldout.model.assetdata

import kotlinx.serialization.Serializable

@Serializable
data class MintExtensions(
    val confidential_transfer_account: ConfidentialTransferAccount? = null,
    val confidential_transfer_fee_config: ConfidentialTransferFeeConfig? = null,
    val confidential_transfer_mint: ConfidentialTransferMint? = null,
    val default_account_state: String? = null,
    val interest_bearing_config: InterestBearingConfig? = null,
    val metadata: MetadataX? = null,
    val metadata_pointer: MetadataPointer? = null,
    val mint_close_authority: MintCloseAuthority? = null,
    val permanent_delegate: PermanentDelegate? = null,
    val transfer_fee_config: TransferFeeConfig? = null,
    val transfer_hook: TransferHook? = null
)