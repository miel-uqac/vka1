package com.example.keyboard

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.lifecycle.asLiveData
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.keyboard.data.Layout
import com.example.keyboard.ui.MainScreen
import com.example.keyboard.ui.SettingsScreen
import com.example.keyboard.ui.SplashScreen

enum class KeyboardScreen {
    Start,
    Main,
    Settings
}

@Composable
fun KeyboardApp(
    viewModel: USBViewModel,
    settingViewModel: SettingModelView,
    navController: NavHostController = rememberNavController(),
    isDarkTheme: Boolean,

    ) {
    val backStackEntry by navController.currentBackStackEntryAsState()
    val connected by viewModel.isConnected.observeAsState(false)
    val hasPermission by viewModel.hasPermission.observeAsState(false)
    val currentLayout =
        settingViewModel.settingPreferences.asLiveData().observeAsState().value?.layout ?: Layout.FR
    KeyboardScreen.valueOf(
        backStackEntry?.destination?.route ?: KeyboardScreen.Start.name
    )

    Scaffold { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = KeyboardScreen.Start.name,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(route = KeyboardScreen.Start.name) {
                SplashScreen(
                    connected = connected,
                    hasPermission = hasPermission,
                    onGranted = { navController.navigate(route = KeyboardScreen.Main.name) },
                    askPermission = viewModel::askPermission
                )
            }
            composable(route = KeyboardScreen.Main.name) {
                MainScreen(
                    sendData = viewModel::writeUSB
                )
            }
            composable(route = KeyboardScreen.Settings.name) {
                SettingsScreen(
                    changeTheme = settingViewModel::updateDarkTheme,
                    isDarkTheme = isDarkTheme,
                    setLayout = settingViewModel::updateLayout,
                    actualLayout = currentLayout,
                    onBackButtonClicked = { navController.navigate(route = KeyboardScreen.Main.name) }
                )
            }
        }
        LaunchedEffect(!hasPermission) {
            if (!hasPermission) {
                navController.navigate(route = KeyboardScreen.Start.name)
            }
        }
    }
}