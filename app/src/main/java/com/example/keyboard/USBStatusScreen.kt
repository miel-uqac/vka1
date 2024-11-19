package com.example.keyboard.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.platform.LocalContext
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.keyboard.USBConnectionViewModel
import com.example.keyboard.USBController

@Composable
fun USBStatusScreen(
    viewModel: USBConnectionViewModel,
    onUSBReady: () -> Unit
) {
    val context = LocalContext.current


    // Obtenir les états USB depuis le ViewModel
    val isConnected by viewModel.isConnected.observeAsState(false)
    val hasPermission by viewModel.hasPermission.observeAsState(false)

    // Vérifier si tout est prêt
    if (hasPermission && isConnected) {
        LaunchedEffect(Unit) {
            viewModel.ManageUSB(context)
            viewModel.connectToDevice(context)
            onUSBReady() // Naviguer vers HomeScreen
        }
    }

    // Affichage conditionnel pour gérer les différents états
    when {
        !hasPermission -> {
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(text = "Permission USB non accordée")
                Button(
                    onClick = { viewModel.ManageUSB(context) }) {
                    Text("Demander la permission")
                }
            }
        }
        !isConnected -> {
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(text = "Aucun appareil USB connecté")
                Button(onClick = { viewModel.checkConnected() }) {
                    Text("Vérifier l'état USB")
                }
            }
        }
        else -> {
            Text(text = "Appareil USB prêt", modifier = Modifier.padding(16.dp))
        }
    }
}
