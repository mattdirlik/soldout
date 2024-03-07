package com.example.soldout.usecase

import android.net.Uri
import com.solana.publickey.SolanaPublicKey
import com.solana.transaction.AccountMeta
import com.solana.transaction.Message
import com.solana.transaction.Transaction
import com.solana.transaction.TransactionInstruction
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

object MemoTransactionUseCase {
    private val TAG = AccountBalanceUseCase::class.simpleName
    private const val memoProgramId = "MemoSq4gqABAXKb96qnH8TysNcWxMyWCqXgDLGmfcHr"

    suspend operator fun invoke(rpcUri: Uri, address: SolanaPublicKey, message: String): Transaction =
        withContext(Dispatchers.IO) {
            // Solana Memo Program
            val memoProgramId = SolanaPublicKey.from(memoProgramId)
            val memoInstruction = TransactionInstruction(
                programId = memoProgramId,
                accounts = listOf(
                    AccountMeta(
                        publicKey = address,
                        isSigner = true,
                        isWritable = true)
                ),
                data = message.encodeToByteArray()
            )

            // Build Message
            val blockhash = RecentBlockhashUseCase(rpcUri)
            val memoTxMessage = Message.Builder()
                .addInstruction(memoInstruction)
                .setRecentBlockhash(blockhash)
                .build()
            return@withContext Transaction(memoTxMessage)
        }
}