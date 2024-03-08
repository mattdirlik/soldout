package com.example.soldout.usecase

import android.net.Uri
import com.example.soldout.model.tokenaccounts.TokenAccountsResponse
import com.example.soldout.networking.KtorHttpDriver
import com.solana.networking.Rpc20Driver
import com.solana.rpccore.JsonRpc20Request
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.put
import java.util.UUID

object TokenAccountsUseCase {
    suspend operator fun invoke(
        rpcUri: Uri,
        owner: String,
        mint: String
    ) {
        withContext(Dispatchers.IO) {
            val rpc = Rpc20Driver(rpcUri.toString(), KtorHttpDriver())
            val requestId = UUID.randomUUID().toString()
            val request = createTokenAccountsRequest(
                owner = owner,
                mint = mint,
                requestId = requestId
            )
            val response = rpc.makeRequest(request, TokenAccountsResponse.serializer())

            return@withContext response.result?.token_accounts
        }
    }

    private fun createTokenAccountsRequest(
        owner: String,
        mint: String,
        requestId: String = "1"
    ) =
        JsonRpc20Request(
            method = "getTokenAccounts",
            params = buildJsonObject {
                put("owner", owner)
                put("mint", mint)
            },
            requestId
        )
}