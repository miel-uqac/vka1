package com.example.keyboard

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import java.io.File

class USBConnectionViewModel(
    private val usbController: USBController,
    private val context: Context
) : ViewModel() {
    private val _hasPermission = MutableLiveData(false)
    val hasPermission: LiveData<Boolean> = _hasPermission

    private val _isConnected = MutableLiveData(false)
    val isConnected: LiveData<Boolean> = _isConnected

    companion object {
        private const val TAG = "USB_DEBUG"
    }

    init {
        ManageUSB(context)
    }


    fun ManageUSB(context: Context) {
        usbController.ManageUSB(context)
        checkConnected()
        checkUSBPermission()
    }

    fun connectToDevice(context: Context) {
        val device = usbController.driver?.device
        if (device != null) {
            usbController.connectToDevice(context, device)
            checkConnected()
            checkUSBPermission()
        } else {
            writeLogToFile(context, TAG, "No USB device detected")
        }
    }


    fun disconnectUSB() {
        usbController.disconnectFromDevice(context)
        checkConnected()
        checkUSBPermission()
    }

    fun writeUSB(data: String) {
        usbController.USBWrite(context, data)
    }

    fun checkConnected() {
        _isConnected.value = usbController.isConnected
        writeLogToFile(context, TAG, "USB connection status: ${_isConnected.value}")
    }

    private fun checkUSBPermission() {
        _hasPermission.value = usbController.hasDevicePermission
        writeLogToFile(context, TAG, "USB permission status: ${_hasPermission.value}")
    }

    fun setIsConnected(value: Boolean) {
        _isConnected.value = value
    }

    fun setHasPermission(value: Boolean) {
        _hasPermission.value = value
    }


    fun writeLogToFile(context: Context, tag: String, message: String) {
        val file = File(context.getExternalFilesDir(null), "usb_logs.txt")
        file.appendText("$tag: $message\n")
    }

}

