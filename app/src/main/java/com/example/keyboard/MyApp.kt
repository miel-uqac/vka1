package com.example.keyboard

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.keyboard.ui.AnimatedSplashScreen
import com.example.keyboard.ui.HomeScreen
import com.example.keyboard.ui.USBStatusScreen

//Fonction pour titre si plusieurs pages
/*@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyAppBar(
) {
    TopAppBar(
        title = { Text("Keyboard ") },
    )
}*/

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyApp(
    viewModel: USBConnectionViewModel,
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController()
) {
    val connected by viewModel.isConnected.observeAsState(false)
    val hasPermission by viewModel.hasPermission.observeAsState(false)

    Scaffold(
        modifier = modifier,
        topBar = {
            TopAppBar(
                title = { Text("Keyboard App") }
            )
        }
    ) { paddingValues ->
        NavHost(navController = navController, startDestination = "splash") {
            composable("splash") {
                AnimatedSplashScreen(
                    onComplete = {
                        navController.navigate("home") {
                        popUpTo("splash") { inclusive = true }
                    }
                    })
            }
            composable("usbStatus") {
                USBStatusScreen(
                    viewModel = viewModel,
                    onUSBReady = {
                        navController.navigate("home") {
                            popUpTo("usbStatus") { inclusive = true }
                        }
                    }
                )
            }
            composable("home") {
                HomeScreen(
                    sendData = viewModel::writeUSB,
                    connected = connected,
                    hasPermission = hasPermission,
                    onUSBDisconnected = {
                        navController.navigate("usbStatus") {
                            popUpTo("home") { inclusive = true }
                        }
                    }
                )
            }
        }
    }
}