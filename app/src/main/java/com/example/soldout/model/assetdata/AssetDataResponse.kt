package com.example.soldout.model.assetdata

import kotlinx.serialization.Serializable

@Serializable
data class AssetDataResponse(
    val authorities: List<Authorities>? = null,
    val compression: Compression? = null,
    val content: Content? = null,
    val creators: List<Creators>? = null,
    val grouping: List<Grouping>? = null,
    val id: String? = null,
    val inscription: Inscription? = null,
    val `interface`: String? = null,
    val mint_extensions: MintExtensions? = null,
    val ownership: Ownership? = null,
    val royalty: Royalty? = null,
    val spl20: Spl20? = null,
    val token_info: TokenInfo? = null
)