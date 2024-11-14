package com.example.keyboard

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.hardware.usb.UsbManager
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.*
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import com.example.keyboard.ui.theme.KeyboardTheme
import com.example.keyboard.ui.AnimatedSplashScreen

class MainActivity : ComponentActivity() {

    private lateinit var usbHandler: USBHandler
    private lateinit var connectionViewModel: USBConnectionViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Initialise
        usbHandler = USBHandler(getSystemService(Context.USB_SERVICE) as UsbManager)
        connectionViewModel = USBConnectionViewModel(usbHandler, this)

        // splash screen + écran principal
        setContent {
            KeyboardTheme {
                MainScreen(connectionViewModel)
            }
        }

        // Enregistrer le récepteur pour détecter la connexion USB
        val filter = IntentFilter().apply {
            addAction(UsbManager.ACTION_USB_DEVICE_ATTACHED)
            addAction(UsbManager.ACTION_USB_DEVICE_DETACHED)
        }
        registerReceiver(usbBroadcastReceiver, filter)
    }

    // Gestionnaire des événements USB
    private val usbBroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent) {
            when (intent.action) {
                UsbManager.ACTION_USB_DEVICE_ATTACHED -> connectionViewModel.initializeConnection()
                UsbManager.ACTION_USB_DEVICE_DETACHED -> connectionViewModel.disconnect()
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(usbBroadcastReceiver)
    }
}

@Composable
fun MainScreen(connectionViewModel: USBConnectionViewModel) {
    // Variable d'état pour contrôler l'affichage du splash screen
    var showSplash by remember { mutableStateOf(true) }

    if (showSplash) {
        AnimatedSplashScreen(onComplete = { showSplash = false })
    } else {
        // Usplash terminé, afficher l'écran
        Scaffold { innerPadding ->
            MyApp(
                viewModel = connectionViewModel,
                modifier = Modifier.padding(innerPadding)
            )
        }
    }
}
