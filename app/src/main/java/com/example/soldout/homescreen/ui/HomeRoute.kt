package com.example.soldout.homescreen.ui

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign.Companion.Center
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.soldout.openUrl
import com.example.soldout.viewmodel.HomeViewModel
import com.solana.mobilewalletadapter.clientlib.ActivityResultSender

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeRoute(
    viewModel: HomeViewModel = hiltViewModel(),
    activityResultSender: ActivityResultSender
) {
    val snackbarHostState = remember { SnackbarHostState() }

    with(viewModel.uiState) {
        Scaffold(
            topBar = {
                Text(
                    text = "Sol'd Out",
                    style = MaterialTheme.typography.titleLarge,
                    textAlign = Center,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(all = 24.dp)
                )
            },
            containerColor = Color.Transparent,
            contentColor = MaterialTheme.colorScheme.onBackground,
            contentWindowInsets = WindowInsets(12, 12, 12, 12),
            snackbarHost = { SnackbarHost(snackbarHostState) },
        ) { padding ->

            LaunchedEffect(Unit) {
                viewModel.loadConnection()
            }

            LaunchedEffect(snackbarMessage) {
                snackbarMessage?.let { message ->
                    snackbarHostState.showSnackbar(message, duration = SnackbarDuration.Short)
                    viewModel.clearSnackBar()
                }
            }

            Column(
                modifier = Modifier.padding(padding)
            ) {

                Section(
                    sectionTitle = "Messages:",
                ) {
                    Button(
                        onClick = {
                            if (canTransact)
                                viewModel.signMessage(activityResultSender, "Hello Solana!")
                            else
                                viewModel.disconnect()
                        },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(text = "Sign a message")
                    }
                }

                Section(
                    sectionTitle = "Transactions:",
                ) {
                    Button(
                        onClick = {
                            if (canTransact)
                                viewModel.signTransaction(activityResultSender)
                            else
                                viewModel.disconnect()
                        },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(text = "Sign a Transaction (deprecated)")
                    }
                    Button(
                        onClick = {
                            if (canTransact)
                                viewModel.publishMemo(activityResultSender, "Hello Solana!")
                            else
                                viewModel.disconnect()
                        },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(text = "Send a Memo Transaction")
                    }

                    memoTxSignature?.let {
                        ExplorerHyperlink(txSignature = it)
                    }
                }

                Spacer(modifier = Modifier.weight(1f))

                if (canTransact) {
                    AccountInfo(
                        walletName = userLabel,
                        address = userAddress,
                        balance = solBalance
                    )
                }

                Button(
                    modifier = Modifier
                        .weight(1f)
                        .padding(start = 4.dp)
                        .fillMaxWidth(),
                    onClick = {
                        if (!canTransact)
                            viewModel.connect(activityResultSender)
                        else
                            viewModel.disconnect()
                    }
                ) {
                    Text(if (canTransact) "Disconnect" else "Connect")
                }
            }
        }
    }
}

@Composable
fun Section(sectionTitle: String, content: @Composable () -> Unit) {
    Column(modifier = Modifier.padding(bottom = 16.dp)) {
        Text(
            text = sectionTitle,
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 16.dp)
        )
        content()
    }
}

@Composable
fun AccountInfo(
    walletName: String,
    address: String,
    balance: Number
) {
    Card(
        shape = RoundedCornerShape(16.dp),
        modifier = Modifier
            .padding(bottom = 8.dp)
            .fillMaxWidth()
            .border(1.dp, Color.Black, RoundedCornerShape(16.dp))
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(
                text = "Connected Wallet",
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Bold,
            )
            // Wallet name and address
            Text(
                text = "$walletName ($address)",
                style = MaterialTheme.typography.bodyMedium,
                color = Color.Gray
            )


            Spacer(modifier = Modifier.height(8.dp))

            // Account balance
            Text(
                text = "$balance SOL", // TODO: Nicely format the displayed number (e.g: 0.089 SOL)
                style = MaterialTheme.typography.headlineLarge
            )
        }
    }
}

@Composable
fun ExplorerHyperlink(txSignature: String) {
    val context = LocalContext.current
    val url = "https://explorer.solana.com/tx/${txSignature}?cluster=devnet"
    val annotatedText = AnnotatedString.Builder("View your memo on the ").apply {
        pushStyle(
            SpanStyle(
                color = Color.Blue,
                textDecoration = TextDecoration.Underline,
                fontSize = 16.sp
            )
        )
        append("explorer.")
    }

    ClickableText(
        text = annotatedText.toAnnotatedString(),
        onClick = {
            openUrl(context, url)
        }
    )
}