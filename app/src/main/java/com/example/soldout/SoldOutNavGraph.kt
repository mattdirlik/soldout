package com.example.soldout

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.soldout.homescreen.ui.HomeRoute
import com.solana.mobilewalletadapter.clientlib.ActivityResultSender

@Composable
fun SoldOutNavGraph(
    navController: NavHostController,
    activityResultSender: ActivityResultSender
) {
    NavHost(
        navController = navController,
        startDestination = "HOME_ROUTE"
    ) {
        composable("HOME_ROUTE") {
            HomeRoute(
                activityResultSender = activityResultSender
            )
        }
    }
}