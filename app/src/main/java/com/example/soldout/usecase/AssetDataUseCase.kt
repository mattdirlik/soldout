package com.example.soldout.usecase

import android.net.Uri
import com.example.soldout.model.assetdata.AssetDataResponse
import com.example.soldout.networking.KtorHttpDriver
import com.solana.networking.Rpc20Driver
import com.solana.rpccore.JsonRpc20Request
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive
import java.util.UUID

object AssetDataUseCase {
    suspend operator fun invoke(rpcUri: Uri, asset: String): String? =
        withContext(Dispatchers.IO) {
            val rpc = Rpc20Driver(rpcUri.toString(), KtorHttpDriver())
            val requestId = UUID.randomUUID().toString()
            val request = createAssetDataRequest(asset, requestId)
            val response = rpc.makeRequest(request, AssetDataResponse.serializer())

            return@withContext response.result?.authorities?.first()?.address
        }

    private fun createAssetDataRequest(asset: String, requestId: String = "1") =
        JsonRpc20Request(
            method = "getAsset",
            params = JsonObject(
                content = mapOf(
                    "id" to JsonPrimitive(asset)
                )
            ),
            requestId
        )
}