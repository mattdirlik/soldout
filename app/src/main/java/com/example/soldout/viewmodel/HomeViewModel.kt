package com.example.soldout.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.core.net.toUri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.soldout.BuildConfig
import com.example.soldout.usecase.AccountBalanceUseCase
import com.example.soldout.usecase.Connected
import com.example.soldout.usecase.MemoTransactionUseCase
import com.example.soldout.usecase.PersistenceUseCase
import com.funkatronics.encoders.Base58
import com.solana.publickey.SolanaPublicKey
import com.solana.mobilewalletadapter.clientlib.ActivityResultSender
import com.solana.mobilewalletadapter.clientlib.MobileWalletAdapter
import com.solana.mobilewalletadapter.clientlib.TransactionResult
import com.solana.mobilewalletadapter.clientlib.successPayload
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val walletAdapter: MobileWalletAdapter,
    private val persistenceUseCase: PersistenceUseCase
): ViewModel() {

    private val rpcUri = BuildConfig.RPC_URI.toUri()

    var uiState by mutableStateOf(HomeUiState())

    fun loadConnection() {
        val persistedConn = persistenceUseCase.getWalletConnection()

        if (persistedConn is Connected) {
            uiState = uiState.copy(
                isLoading = true,
                canTransact = true,
                userAddress = persistedConn.publicKey.base58(),
                userLabel = persistedConn.accountLabel,
            )

            getBalance(persistedConn.publicKey)

            uiState = uiState.copy(
                isLoading = false,
                // TODO: Move all Snackbar message strings into resources
                snackbarMessage = "✅ | Successfully auto-connected to: \n" + persistedConn.publicKey.base58() + "."
            )

            walletAdapter.authToken = persistedConn.authToken
        }
    }

    fun connect(sender: ActivityResultSender) {
        viewModelScope.launch {
            when (val result = walletAdapter.connect(sender)) {
                is TransactionResult.Success -> {
                    val currentConn = Connected(
                        SolanaPublicKey(result.authResult.accounts.first().publicKey),
                        result.authResult.accounts.first().accountLabel ?: "",
                        result.authResult.authToken
                    )

                    persistenceUseCase.persistConnection(
                        currentConn.publicKey,
                        currentConn.accountLabel,
                        currentConn.authToken
                    )

                    uiState = uiState.copy(
                        isLoading = true,
                        userAddress = currentConn.publicKey.base58(),
                        userLabel = currentConn.accountLabel
                    )

                    getBalance(currentConn.publicKey)

                    uiState = uiState.copy(
                        isLoading = false,
                        canTransact = true,
                        snackbarMessage = "✅ | Successfully connected to: \n" + currentConn.publicKey.base58() + "."
                    )
                }

                is TransactionResult.NoWalletFound -> {
                    uiState = uiState.copy(
                        walletFound = false,
                        snackbarMessage = "❌ | No wallet found."
                    )

                }

                is TransactionResult.Failure -> {
                    uiState = uiState.copy(
                        isLoading = false,
                        canTransact = false,
                        userAddress = "",
                        userLabel = "",
                        snackbarMessage = "❌ | Failed connecting to wallet: " + result.e.message
                    )
                }
            }
        }
    }

    fun signMessage(sender: ActivityResultSender, message: String) {
        viewModelScope.launch {
            val result = walletAdapter.transact(sender) {
                signMessagesDetached(arrayOf(message.toByteArray()), arrayOf((it.accounts.first().publicKey)))
            }

            uiState = when (result) {
                is TransactionResult.Success -> {
                    val signatureBytes = result.successPayload?.messages?.first()?.signatures?.first()
                    uiState.copy(
                        snackbarMessage = signatureBytes?.let {
                            "✅ | Message signed: ${Base58.encodeToString(it)}"
                        } ?: "❌ | Incorrect payload returned"
                    )
                }
                is TransactionResult.NoWalletFound -> {
                    uiState.copy(snackbarMessage = "❌ | No wallet found")
                }
                is TransactionResult.Failure -> {
                    uiState.copy(snackbarMessage = "❌ | Message signing failed: ${result.e.message}")
                }
            }
        }
    }

    fun signTransaction(sender: ActivityResultSender ) {
        viewModelScope.launch {
            val result = walletAdapter.transact(sender) { authResult ->
                val account = SolanaPublicKey(authResult.accounts.first().publicKey)
                val memoTx = MemoTransactionUseCase(rpcUri, account, "Hello Solana!")
                signTransactions(arrayOf(
                    memoTx.serialize(),
                ))
            }

            uiState = when (result) {
                is TransactionResult.Success -> {
                    val signedTxBytes = result.successPayload?.signedPayloads?.first()
                    signedTxBytes?.let {
                        println("Memo publish signature: " + Base58.encodeToString(signedTxBytes))
                    }
                    uiState.copy(
                        snackbarMessage = (signedTxBytes?.let {
                            "✅ | Transaction signed: ${Base58.encodeToString(it)}"
                        } ?: "❌ | Incorrect payload returned"),
                    )
                }
                is TransactionResult.NoWalletFound -> {
                    uiState.copy(snackbarMessage = "❌ | No wallet found")
                }
                is TransactionResult.Failure -> {
                    uiState.copy(snackbarMessage = "❌ | Transaction failed to submit: ${result.e.message}")
                }
            }
        }
    }

    fun publishMemo(sender: ActivityResultSender, memoText: String) {
        viewModelScope.launch {
            val result = walletAdapter.transact(sender) { authResult ->
                val account = SolanaPublicKey(authResult.accounts.first().publicKey)
                val memoTx = MemoTransactionUseCase(rpcUri, account, memoText)
                signAndSendTransactions(arrayOf(memoTx.serialize()))
            }

            uiState = when (result) {
                is TransactionResult.Success -> {
                    val signatureBytes = result.successPayload?.signatures?.first()
                    signatureBytes?.let {
                        println("Memo publish signature: " + Base58.encodeToString(signatureBytes))
                        uiState.copy(
                            snackbarMessage = "✅ | Transaction submitted: ${Base58.encodeToString(it)}",
                            memoTxSignature = Base58.encodeToString(it)
                        )
                    } ?: uiState.copy(
                        snackbarMessage = "❌ | Incorrect payload returned"
                    )
                }
                is TransactionResult.NoWalletFound -> {
                    uiState.copy(snackbarMessage = "❌ | No wallet found")
                }
                is TransactionResult.Failure -> {
                    uiState.copy(snackbarMessage = "❌ | Transaction failed to submit: ${result.e.message}")
                }
            }
        }
    }

    private fun getBalance(account: SolanaPublicKey) {
        viewModelScope.launch(Dispatchers.IO) {
            uiState = try {
                val result =
                    AccountBalanceUseCase(rpcUri, account)

                uiState.copy(
                    solBalance = result/1000000000.0
                )
            } catch (e: Exception) {
                uiState.copy(
                    snackbarMessage = "❌ | Failed fetching account balance."
                )
            }
        }
    }

    fun disconnect() {
        viewModelScope.launch {
            val conn = persistenceUseCase.getWalletConnection()
            if (conn is Connected) {
                persistenceUseCase.clearConnection()

                uiState = HomeUiState(
                    snackbarMessage = "✅ | Disconnected from wallet."
                )
            }
        }
    }

    fun clearSnackBar() {
        uiState = uiState.copy(
            snackbarMessage = null
        )
    }
}