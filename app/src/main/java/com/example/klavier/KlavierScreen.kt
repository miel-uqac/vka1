/*
package com.example.klavier

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.asLiveData
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.klavier.data.Layout
import com.example.klavier.ui.MainScreen
import com.example.klavier.ui.SettingsScreen
import com.example.klavier.ui.SplashScreen

enum class KlavierScreen(@StringRes val title: Int) {
    Start(title = R.string.splash_screen),
    Main(title = R.string.app_name),
    Settings(title = R.string.settings)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun KlavierAppBar(
    currentScreen: KlavierScreen,
    canNavigateBack: Boolean,
    navigateUp: () -> Unit,
    modifier: Modifier = Modifier
) {
    TopAppBar(
        title = { Text(stringResource(currentScreen.title)) },
        modifier = modifier,
        navigationIcon = {
            if (canNavigateBack) {
                IconButton(onClick = navigateUp) {
                    Icon(
                        imageVector = Icons.Filled.Settings,//Icons.Filled.ArrowBack,
                        contentDescription = "settings"//stringResource(R.string.back_button)
                    )
                }
            }
        }
    )
}

@Composable
fun KlavierApp(
    viewModel: USBViewModel,
    SettingViewModel: SettingModelView,
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
    isDarkTheme: Boolean,

    ) {
    val backStackEntry by navController.currentBackStackEntryAsState()
    val connected by viewModel.isConnected.observeAsState(false)
    val hasPermission by viewModel.hasPermission.observeAsState(false)
    val currentLayout = SettingViewModel.settingPreferences.asLiveData().observeAsState().value?.layout ?: Layout.FR
    val currentScreen = KlavierScreen.valueOf(
        backStackEntry?.destination?.route ?: KlavierScreen.Start.name
    )

    var macroLabels by remember { mutableStateOf(ArrayList<String>()) }
    var macroIcons by remember { mutableStateOf(ArrayList<Int>()) }
    var macroFunctions by remember { mutableStateOf(ArrayList<() -> Unit>()) }

    Scaffold(
       */
/* topBar = {
            KlavierAppBar(
                currentScreen = currentScreen,
                canNavigateBack = navController.previousBackStackEntry != null,
                navigateUp = { navController.navigateUp() }
            )
        }*//*

    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = KlavierScreen.Start.name,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(route = KlavierScreen.Start.name) {
                SplashScreen(
                    connected = connected,
                    hasPermission = hasPermission,
                    onGranted = { navController.navigate(route = KlavierScreen.Main.name) },
                    askPermission = viewModel::askPermission
                )
            }
            composable(route = KlavierScreen.Main.name) {
                MainScreen(
                    sendData = viewModel::writeUSB,
                    onSettingsButtonClicked = { navController.navigate(route = KlavierScreen.Settings.name) },
                    macroLabels,
                    macroIcons,
                    macroFunctions
                )
            }
            composable(route = KlavierScreen.Settings.name) {
                SettingsScreen(
                    ChangeTheme = SettingViewModel::updateDarkTheme,
                    isDarkTheme = isDarkTheme,
                    SetLayout = SettingViewModel::updateLayout,
                    actualLayout = currentLayout,
                    onBackButtonClicked = { navController.navigate(route = KlavierScreen.Main.name) }
                )
            }
        }
        LaunchedEffect(!hasPermission) {
            if(!hasPermission) {
                navController.navigate(route =KlavierScreen.Start.name)
            }
        }
    }
}*/