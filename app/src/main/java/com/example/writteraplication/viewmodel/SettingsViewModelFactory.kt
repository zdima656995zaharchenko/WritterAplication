
package com.example.writteraplication.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.writteraplication.data.preferences.ThemePreferenceManager

class SettingsViewModelFactory(
    private val themePreferenceManager: ThemePreferenceManager
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SettingsViewModel::class.java)) {
            return SettingsViewModel(themePreferenceManager) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}


