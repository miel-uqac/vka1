package com.example.keyboard

import android.hardware.usb.UsbManager
import android.util.Log
import com.hoho.android.usbserial.driver.UsbSerialPort
import com.hoho.android.usbserial.driver.UsbSerialProber

class USBHandler(private val usbManager: UsbManager) {

    private var serialConnection: UsbSerialPort? = null

    fun initializeDevice() {
        val drivers = UsbSerialProber.getDefaultProber().findAllDrivers(usbManager)
        if (drivers.isNotEmpty()) {
            val driver = drivers[0]
            val connection = usbManager.openDevice(driver.device)
            serialConnection = driver.ports[0].apply {
                open(connection)
                setParameters(9600, 8, UsbSerialPort.STOPBITS_1, UsbSerialPort.PARITY_NONE)
            }
        } else {
            Log.d("USBHandler", "No device available.")
        }
    }

    fun sendData(data: String) {
        serialConnection?.let {
            it.write(data.toByteArray(), 500)
        } ?: Log.e("USBHandler", "No active connection.")
    }


    fun terminateConnection() {
        serialConnection?.close()
    }
}
