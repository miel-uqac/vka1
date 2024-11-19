package com.example.keyboard

import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.hardware.usb.UsbConstants
import android.hardware.usb.UsbDevice
import android.hardware.usb.UsbDeviceConnection
import android.hardware.usb.UsbManager
import android.os.Build
import android.util.Log
import com.hoho.android.usbserial.driver.CdcAcmSerialDriver
import com.hoho.android.usbserial.driver.ProbeTable
import com.hoho.android.usbserial.driver.UsbSerialDriver
import com.hoho.android.usbserial.driver.UsbSerialPort
import com.hoho.android.usbserial.driver.UsbSerialProber
import java.io.File

class USBController(private val usbManager: UsbManager) {
    var m_driver: UsbSerialDriver? = null
    private var m_device: UsbDevice? = null
    var m_connection: UsbDeviceConnection? = null
    var m_port: UsbSerialPort? = null
    var hasDevicePermission = false
    var isConnected = false

    val ACTION_USB_PERMISSION = "com.android.example.USB_PERMISSION"
    val WRITE_WAIT_MILLIS = 1000
    private val grantedDevices = mutableSetOf<UsbDevice>()



    companion object {
        private const val TAG = "USB_DEBUG"
    }

    fun ManageUSB(context: Context) {
        val customTable = ProbeTable()
        customTable.addProduct(
            10374,
            69,
            CdcAcmSerialDriver::class.java
        ) // A Ajuster selon le device
        val customProber = UsbSerialProber(customTable)

        val availableDrivers = customProber.findAllDrivers(usbManager)
        if (availableDrivers.isEmpty()) {
            writeLogToFile(context, TAG, "No USB drivers found or no device connected ")
            return
        }
        m_driver = availableDrivers[0]
        m_device = m_driver?.device


        if (m_driver == null) {
            writeLogToFile(context, TAG, "No drivers found for the connected device.")
        } else {
            writeLogToFile(context, TAG, "Driver found: ${m_driver?.device?.deviceName}")
        }
    }


    fun askPermission(context: Context, device : UsbDevice) {

        if (grantedDevices.contains(device)) {
            writeLogToFile(context, TAG, "Permission already granted for device: ${device.deviceName}")
            hasDevicePermission = true
            return
        }

        grantedDevices.add(device)


        if (m_driver == null || m_driver?.device == null) {
            writeLogToFile(context, TAG, "Driver or device is null, cannot request permission")
            return
        }

        val device = m_driver!!.device
        if (grantedDevices.contains(device)) {
            writeLogToFile(context, TAG, "Permission already tracked for device: ${device.deviceName}")
            hasDevicePermission = true
            return
        }

        /*val flags =
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) PendingIntent.FLAG_MUTABLE else 0
        val intent: PendingIntent = if (Build.VERSION.SDK_INT >= 33

        ) {
            val intent = Intent(ACTION_USB_PERMISSION)
            intent.setPackage(context.packageName)
            PendingIntent.getBroadcast(context, 0, intent, flags)
        } else {
            PendingIntent.getBroadcast(context, 0, Intent(ACTION_USB_PERMISSION), flags)
        }*/

        val flags = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) PendingIntent.FLAG_MUTABLE else 0
        val intent = if (Build.VERSION.SDK_INT >= 33) {
            Intent(ACTION_USB_PERMISSION).apply { setPackage(context.packageName) }
        } else {
            Intent(ACTION_USB_PERMISSION)
        }
        val pendingIntent = PendingIntent.getBroadcast(context, 0, intent, flags)

        usbManager.requestPermission(m_driver!!.device, pendingIntent)
        writeLogToFile(
            context,
            TAG,
            "Requesting permission for device: ${m_driver?.device?.deviceName}"
        )

        // Si m_driver ou m_driver.device est null, ajoute un log pour débugguer
        /*if (m_driver == null || m_driver?.device == null) {
            writeLogToFile(context, TAG, "Driver or device is null, cannot request permission")
            return
        }

        usbManager.requestPermission(m_driver!!.device, intent)
        writeLogToFile(context, TAG, "Permission request sent")*/
    }

    fun checkAllInterfaces(context: Context) {
        val usbDevice = m_driver?.device ?: return
        for (i in 0 until usbDevice.interfaceCount) {
            val usbInterface = usbDevice.getInterface(i)
            writeLogToFile(
                context,
                TAG,
                "Interface $i: Class=${usbInterface.interfaceClass}, Subclass=${usbInterface.interfaceSubclass}"
            )
            if (usbInterface.interfaceClass == UsbConstants.USB_CLASS_COMM ||
                usbInterface.interfaceClass == UsbConstants.USB_CLASS_CDC_DATA
            ) {
                if (m_connection?.claimInterface(usbInterface, true) == true) {
                    writeLogToFile(context, TAG, "Successfully claimed interface $i")
                } else {
                    writeLogToFile(context, TAG, "Failed to claim interface $i")
                }
            } else {
                writeLogToFile(context, TAG, "Skipping interface $i: Class=${usbInterface.interfaceClass}")
            }
        }
    }




    fun USBWrite(context: Context, data: String) {
        Log.i(TAG, m_port.toString())
        writeLogToFile(context, TAG, m_port.toString())
        m_port?.write((data + "\r\n").toByteArray(), WRITE_WAIT_MILLIS)
        Log.i(TAG, data)
        writeLogToFile(context, TAG, data)

    }


/*
    fun connectToDevice(context: Context) {
        try {
            if (m_connection != null) {
                writeLogToFile(context, TAG, "Connection already exists, skipping new connection")
                return
            }

            if (m_driver == null || m_driver?.device == null) {
                writeLogToFile(context, TAG, "Driver or device is null, cannot connect")
                return
            }

            val usbDevice = m_driver?.device
            if (usbDevice != null) {
                writeLogToFile(context, TAG, "Device has ${usbDevice.interfaceCount} interfaces:")
                for (i in 0 until usbDevice.interfaceCount) {
                    val usbInterface = usbDevice.getInterface(i)
                    writeLogToFile(
                        context, TAG,
                        "Interface $i: ID=${usbInterface.id}, Class=${usbInterface.interfaceClass}, Subclass=${usbInterface.interfaceSubclass}, Protocol=${usbInterface.interfaceProtocol}"
                    )
                    if (usbInterface.interfaceClass == UsbConstants.USB_CLASS_COMM) {
                        if (m_connection?.claimInterface(usbInterface, true) == true) {
                            writeLogToFile(context, TAG, "Interface claimed successfully")
                        } else {
                            writeLogToFile(context, TAG, "Failed to claim interface")
                        }
                    }
                }
            }

            writeLogToFile(context, TAG, "Attempting to open USB connection...")
            val usbConnection = usbManager.openDevice(m_driver?.device)
            if (usbConnection == null) {
                writeLogToFile(context, TAG, "Failed to open connection")
                return
            }

            m_connection = usbConnection
            writeLogToFile(context, TAG, "list des ports : ${m_driver?.ports}")
            val selectedPort = m_driver?.ports?.get(0)
            writeLogToFile(context, TAG, "Port found: $m_port")
            selectedPort?.open(m_connection)
            selectedPort?.setParameters(
                115200,
                8,
                UsbSerialPort.STOPBITS_1,
                UsbSerialPort.PARITY_NONE
            )
            writeLogToFile(
                context,
                TAG,
                "Connection successfully established on port: $selectedPort"
            )
        } catch (e: Exception) {
            writeLogToFile(context, TAG, "Error during connection: ${e.message}")
        }

    }
*/

    fun connectToDevice(context: Context, device: UsbDevice) {
        if (isConnected) {
            writeLogToFile(context, TAG, "Device is already connected. Skipping connection.")
            isConnected = true
            return
        }

        try {
            if (usbManager.openDevice(device) == null) {
                writeLogToFile(context, TAG, "Device is no longer available")
                isConnected = false
                return
            }


            m_connection = usbManager.openDevice(device)
            if (m_connection == null) {
                writeLogToFile(context, TAG, "Failed to open connection")
                isConnected = false
                return
            }

            checkAllInterfaces(context)

            m_port = m_driver?.ports?.get(0)
            m_port?.open(m_connection)
            m_port?.setParameters(115200, 8, UsbSerialPort.STOPBITS_1, UsbSerialPort.PARITY_NONE)

            isConnected = true
            hasDevicePermission = true

            writeLogToFile(context, TAG, "Connection successfully established")
        } catch (e: Exception) {
            writeLogToFile(context, TAG, "Error during connection: ${e.message}")
            isConnected = false
        }
    }


    fun disconnectFromDevice(context: Context) {
        try {
            m_port?.close()
            m_connection?.close()
            isConnected = false
            hasDevicePermission = false
            m_driver = null
            m_port = null
            m_connection = null
            writeLogToFile(context, TAG, "Device disconnected successfully")
        } catch (e: Exception) {
            writeLogToFile(context, TAG, "Error during disconnection: ${e.message}")
        }
    }


    fun writeLogToFile(context: Context, tag: String, message: String) {
        val file = File(context.getExternalFilesDir(null), "usb_logs.txt")
        file.appendText("$tag: $message\n")
    }

    val driver: UsbSerialDriver?
        get() = m_driver


}
