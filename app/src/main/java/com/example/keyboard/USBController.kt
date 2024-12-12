package com.example.keyboard

import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.hardware.usb.UsbDevice
import android.hardware.usb.UsbDeviceConnection
import android.hardware.usb.UsbManager
import android.os.Build
import android.util.Log
import com.hoho.android.usbserial.driver.UsbSerialDriver
import com.hoho.android.usbserial.driver.UsbSerialPort
import com.hoho.android.usbserial.driver.UsbSerialProber

class USBController(private val usbManager: UsbManager) {

    private var mDriver: UsbSerialDriver? = null
    private var mDevice: UsbDevice? = null
    private var mConnection: UsbDeviceConnection? = null
    private var mPort: UsbSerialPort? = null
    var hasDevicePermission = false
    var usbConnected = false
    private val ACTION_USB_PERMISSION = "com.android.example.USB_PERMISSION"
    private val WRITE_WAIT_MILLIS = 2000

    fun manageUSB(context: Context) {
        val availableDrivers: List<UsbSerialDriver> =
            UsbSerialProber.getDefaultProber().findAllDrivers(usbManager)
        if (availableDrivers.isEmpty()) {
            Log.i("USB", "No drivers found or no device connected")
            return
        }
        usbConnected = true
        mDriver = availableDrivers[0]
        if (mDriver != null) {
            mDevice = mDriver!!.device
            Log.i("USB", "USB device name: " + mDevice!!.deviceName)
            hasDevicePermission = usbManager.hasPermission(mDriver!!.device)
            if (hasDevicePermission) {
                // Permission already granted, no need to request
                connectToDevice()  // Skip to opening the connection
                Log.i("USB", "USB permission not requested")
            } else {
                // Request permission
                askPermission(context)
                Log.i("USB", "USB permission requested")
            }

        }
    }

    fun usbWrite(data: String) {
        Log.i("USB", mPort.toString())
        mPort?.write((data + "\r\n").toByteArray(), WRITE_WAIT_MILLIS)
        Log.i("USB", data)
    }

    fun askPermission(context: Context) {
        val flags =
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) PendingIntent.FLAG_MUTABLE else 0
        val intent: PendingIntent = if (Build.VERSION.SDK_INT >= 33 /* Android 14.0 (U) */) {
            val intent = Intent(ACTION_USB_PERMISSION)
            intent.setPackage(context.packageName)
            PendingIntent.getBroadcast(context, 0, intent, flags)
        } else {
            PendingIntent.getBroadcast(context, 0, Intent(ACTION_USB_PERMISSION), flags)
        }
        usbManager.requestPermission(mDriver!!.device, intent)
    }

    fun connectToDevice() {
        hasDevicePermission = usbManager.hasPermission(mDriver!!.device)
        if (!hasDevicePermission) {
            Log.i("USB", "USB permission not granted")
            return
        }
        mConnection = usbManager.openDevice(mDriver?.device)
        mPort = mDriver?.ports?.get(0)
        if (mPort != null) {
            mPort!!.open(mConnection)
            mPort!!.setParameters(115200, 8, UsbSerialPort.STOPBITS_1, UsbSerialPort.PARITY_NONE)
        } else {
            Log.i("USB", "port is null")
        }
    }

    fun disconnectFromDevice() {
        mPort?.close()
        mConnection?.close()
        usbConnected = false
        hasDevicePermission = false
        Log.i("USB", "USB disconnected")
    }

}