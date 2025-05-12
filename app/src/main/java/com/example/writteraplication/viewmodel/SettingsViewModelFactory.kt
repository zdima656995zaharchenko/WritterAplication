
package com.example.writteraplication.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.writteraplication.data.preferences.ThemePreferenceManager
import com.example.writteraplication.data.preferences.LanguagePreferenceManager

class SettingsViewModelFactory(
    private val themePreferenceManager: ThemePreferenceManager,
    private val languagePreferenceManager: LanguagePreferenceManager
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SettingsViewModel::class.java)) {
            return SettingsViewModel(themePreferenceManager, languagePreferenceManager) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
