package com.example.keyboard

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class USBConnectionViewModel(private val usbHandler: USBHandler, context: Context) : ViewModel() {

    private val _connected = MutableLiveData(false)
    val connected: LiveData<Boolean> = _connected

    fun initializeConnection() {
        usbHandler.initializeDevice()
        _connected.value = true
    }

    fun disconnect() {
        usbHandler.terminateConnection()
        _connected.value = false
    }

    fun transmitData(data: String) {
        usbHandler.sendData(data)
    }

}
