package com.example.keyboard

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class USBViewModel(private val usbController: USBController, context: Context) : ViewModel() {

    private var _hasPermission = MutableLiveData(false)
    val hasPermission: LiveData<Boolean> = _hasPermission

    private var _isConnected = MutableLiveData(false)
    val isConnected: LiveData<Boolean> = _isConnected

    init {
        manageUSB(context)
    }

    fun manageUSB(context: Context) {
        usbController.manageUSB(context)
        checkConnected()
        checkUSBPermission()
    }

    fun connectToDevice() {
        usbController.connectToDevice()
        checkConnected()
        checkUSBPermission()
    }

    fun disconnectUSB() {
        usbController.disconnectFromDevice()
        checkConnected()
        checkUSBPermission()
    }

    fun writeUSB(data: String) {
        usbController.usbWrite(data)
    }

    fun askPermission(context: Context) {
        usbController.askPermission(context)
    }

    private fun checkConnected() {
        _isConnected.value = usbController.usbConnected
    }

    private fun checkUSBPermission() {
        _hasPermission.value = usbController.hasDevicePermission
    }

}