package fr.sheiizu.vka1

import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.hardware.usb.UsbManager
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.hoho.android.usbserial.driver.UsbSerialPort
import com.hoho.android.usbserial.driver.UsbSerialProber
import com.hoho.android.usbserial.util.SerialInputOutputManager
import java.nio.charset.StandardCharsets
import java.util.concurrent.Executors

class MainActivity : AppCompatActivity() {

    private lateinit var usbManager: UsbManager
    private var serialPort: UsbSerialPort? = null
    private val ACTION_USB_PERMISSION = "fr.sheiizu.vka1.USB_PERMISSION"
    private var isUsbConnected = false // Flag pour suivre l'état de connexion USB

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        usbManager = getSystemService(Context.USB_SERVICE) as UsbManager

        val editText = findViewById<EditText>(R.id.editText)
        val sendButton = findViewById<Button>(R.id.sendButton)
        val statusText = findViewById<TextView>(R.id.statusText)

        // Configure le bouton d'envoi
        sendButton.setOnClickListener {
            val message = editText.text.toString()
            if (message.isNotEmpty()) {
                if (isUsbConnected) {
                    sendCommand(message)
                    statusText.text = "Message envoyé : $message"
                } else {
                    statusText.text = "Aucun appareil USB connecté."
                    Log.e("USB", "Tentative d'envoi sans connexion USB.")
                }
            } else {
                statusText.text = "Le champ texte est vide."
            }
        }

        // Initialisation USB lors du démarrage
        setupUsbConnection()
    }

    private fun setupUsbConnection() {
        val availableDrivers = UsbSerialProber.getDefaultProber().findAllDrivers(usbManager)
        if (availableDrivers.isEmpty()) {
            Log.e("USB", "Aucun périphérique USB trouvé.")
            isUsbConnected = false // Mise à jour du flag
            return
        }

        val driver = availableDrivers[0]
        val connection = usbManager.openDevice(driver.device)
            ?: run {
                // Demande de permission USB
                val permissionIntent = PendingIntent.getBroadcast(
                    this,
                    0,
                    Intent(ACTION_USB_PERMISSION),
                    PendingIntent.FLAG_IMMUTABLE
                )
                usbManager.requestPermission(driver.device, permissionIntent)
                Log.e("USB", "Permission USB requise.")
                isUsbConnected = false // Mise à jour du flag
                return
            }

        // Ouvre et configure le port
        serialPort = driver.ports[0]
        serialPort?.open(connection)
        serialPort?.setParameters(9600, 8, UsbSerialPort.STOPBITS_1, UsbSerialPort.PARITY_NONE)
        isUsbConnected = true // Mise à jour du flag

        // Configure la lecture asynchrone
        val ioManager = SerialInputOutputManager(serialPort, object : SerialInputOutputManager.Listener {
            override fun onNewData(data: ByteArray) {
                runOnUiThread {
                    val response = String(data, StandardCharsets.UTF_8)
                    Log.d("USB", "Réponse reçue : $response")
                }
            }

            override fun onRunError(e: Exception) {
                Log.e("USB", "Erreur dans le gestionnaire de lecture : ${e.message}")
            }
        })
        Executors.newSingleThreadExecutor().submit(ioManager)

        Log.d("USB", "Connexion USB établie avec succès.")
    }

    private fun sendCommand(command: String) {
        try {
            if (serialPort != null && isUsbConnected) {
                serialPort?.write("$command\n".toByteArray(), 1000)
                Log.d("USB", "Commande envoyée : $command")
            } else {
                Log.e("USB", "Erreur : Port USB non connecté ou non disponible.")
            }
        } catch (e: Exception) {
            Log.e("USB", "Erreur lors de l'envoi : ${e.message}")
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        serialPort?.close()
        isUsbConnected = false
    }
}