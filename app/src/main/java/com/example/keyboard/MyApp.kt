package com.example.keyboard

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.keyboard.ui.HomeScreen
import com.example.keyboard.ui.ConfigScreen

@Composable
fun MyApp(viewModel: USBConnectionViewModel, modifier: Modifier = Modifier) {
    val navController = rememberNavController()
    Scaffold { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = "home",
            modifier = modifier.padding(innerPadding)
        ) {
            composable(route = "home") {
                HomeScreen(navController = navController, viewModel = viewModel)
            }
            composable(route = "config") {
                ConfigScreen(navController = navController)
            }
        }
    }
}


