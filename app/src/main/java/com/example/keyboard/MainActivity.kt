package com.example.keyboard

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.hardware.usb.UsbDevice
import android.hardware.usb.UsbManager
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import com.example.keyboard.ui.theme.KeyboardTheme
import com.hoho.android.usbserial.driver.UsbSerialProber
import java.io.File

class MainActivity : ComponentActivity() {
    companion object {
        private const val TAG = "USB_DEBUG"
        private const val ACTION_USB_PERMISSION = "com.example.USB_PERMISSION"
    }

    private lateinit var usbController: USBController
    lateinit var viewModel: USBConnectionViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //clera logs
        /*val file = File(this.getExternalFilesDir(null), "usb_logs.txt")
        file.writeText("")*/


        // Initialisation des composants USB
        usbController = USBController(usbManager = getSystemService(USB_SERVICE) as UsbManager)
        viewModel = USBConnectionViewModel(usbController, this)

        // Configuration de l'interface utilisateur
        enableEdgeToEdge()
        setContent {
            KeyboardTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    MyApp(
                        viewModel = viewModel,
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }

        val usbManager = getSystemService(Context.USB_SERVICE) as UsbManager
        val deviceList = usbManager.deviceList
        if (deviceList.isEmpty()) {
            writeLogToFile(this, TAG, "No USB devices detected")
        } else {
            for ((_, device) in deviceList) {
                writeLogToFile(
                    this,
                    TAG,
                    "Found device: ${device.deviceName}, Vendor: ${device.vendorId}, Product: ${device.productId}"
                )
            }
        }


        // Enregistrer le BroadcastReceiver pour les événements USB
        val filter = IntentFilter().apply {
            addAction(ACTION_USB_PERMISSION)
            addAction(UsbManager.ACTION_USB_DEVICE_ATTACHED)
            addAction(UsbManager.ACTION_USB_DEVICE_DETACHED)
        }
        registerReceiver(broadcastReceiver, filter)

    }

    // Gestionnaire des événements USB
    private val broadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            when (intent.action) {
                ACTION_USB_PERMISSION -> handleUsbPermission(intent)
                UsbManager.ACTION_USB_DEVICE_ATTACHED -> {
                    writeLogToFile(context, TAG, "Device attached")
                    val usbDevice = intent.getParcelableExtra<UsbDevice>(UsbManager.EXTRA_DEVICE)
                    if (usbDevice != null) {
                        writeLogToFile(
                            context,
                            TAG,
                            "Permission already granted for attached device: ${usbDevice.deviceName}"
                        )
                        viewModel.ManageUSB(context)
                    } else {
                        writeLogToFile(context, TAG, "No device detected on attachment")
                    }
                }

                UsbManager.ACTION_USB_DEVICE_DETACHED -> {
                    writeLogToFile(context, TAG, "Device detached")
                    viewModel.disconnectUSB()
                }
            }
        }
    }


    // Gère les permissions USB accordées ou refusées
    private fun handleUsbPermission(intent: Intent) {
        val granted = intent.extras?.getBoolean(UsbManager.EXTRA_PERMISSION_GRANTED) ?: false
        if (granted) {
            try {
                viewModel.connectToDevice(this)
            } catch (e: Exception) {
                Log.e(TAG, "Error connecting to device: ${e.message}")
            }
        } else {
            Log.i("USB", "Permission denied for device")

        }
    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(broadcastReceiver)
    }


    private fun writeLogToFile(context: Context, tag: String, message: String) {
        val file = File(context.getExternalFilesDir(null), "usb_logs.txt")
        file.appendText("$tag: $message\n")
    }
}
