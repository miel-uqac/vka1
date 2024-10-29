package com.example.keyboard

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.keyboard.ui.theme.KeyboardTheme
import androidx.compose.ui.Alignment
import android.content.Context


import android.hardware.usb.UsbDevice
import android.hardware.usb.UsbDeviceConnection
import android.hardware.usb.UsbEndpoint
import android.hardware.usb.UsbManager
import android.hardware.usb.UsbConstants
import androidx.core.content.ContextCompat



class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            KeyboardTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    BluetoothKeyboardUI(
                        modifier = Modifier.padding(innerPadding),
                        context = this
                    )
                }
            }
        }
    }
}

@Composable
fun BluetoothKeyboardUI(modifier: Modifier = Modifier, context: Context) {
    var textState by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        OutlinedTextField(
            value = textState,
            onValueChange = { newText -> textState = newText },
            label = { Text("Tapez votre texte") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                // Appel de la fonction pour envoyer le texte via USB
                sendTextToCircuitPy(context, textState)
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = "Envoyer")
        }
    }
}

// Fonction pour envoyer du texte via USB à CIRCUITPY
// MAIS PAS SUR, TEST DE USB CAR ECHEC VIA BLUETOOTH :(
private fun sendTextToCircuitPy(context: Context, message: String) {
    // Gestionnaire USB pour accéder au périphérique CIRCUITPY
    val usbManager = ContextCompat.getSystemService(context, UsbManager::class.java) as UsbManager
    val deviceList: HashMap<String, UsbDevice> = usbManager.deviceList
    var device: UsbDevice? = null

    // Recherche du périphérique CIRCUITPY par son nom
    for (d in deviceList.values) {
        if (d.productName?.contains("CIRCUITPY6ed3", ignoreCase = true) == true) {
            device = d
            break
        }
    }

    if (device != null) {
        println("Périphérique CIRCUITPY trouvé")
    } else {
        println("Périphérique CIRCUITPY non trouvé")
    }
}



/*@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    KeyboardTheme {
        BluetoothKeyboardUI()
    }
}*/
