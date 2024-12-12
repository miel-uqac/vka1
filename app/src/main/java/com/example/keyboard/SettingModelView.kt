package com.example.keyboard

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.keyboard.data.Layout
import com.example.keyboard.data.SettingPreferenceRepository
import kotlinx.coroutines.launch


class SettingModelView(
    private val settingPreferencesRepository: SettingPreferenceRepository,
    private val sendData: (String) -> Unit, private val context: Context
) : ViewModel() {

    val settingPreferences = settingPreferencesRepository.settingPreferencesFlow

    fun updateDarkTheme(isDarkTheme: Boolean) {
        viewModelScope.launch {
            settingPreferencesRepository.updateDarkTheme(isDarkTheme)
        }

    }

    fun updateLayout(layout: Layout) {
        viewModelScope.launch {
            settingPreferencesRepository.updateLayout(layout)
            sendData(context.getString(R.string.id_layout) + layout.ordinal.toString())
        }
    }
}