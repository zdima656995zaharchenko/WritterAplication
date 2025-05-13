
package com.example.writteraplication.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.writteraplication.data.preferences.ThemePreferenceManager
import com.example.writteraplication.data.preferences.LanguagePreferenceManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.collect

class SettingsViewModel(
    private val themePreferenceManager: ThemePreferenceManager,
    private val languagePreferenceManager: LanguagePreferenceManager
) : ViewModel() {

    private val _isDarkTheme = MutableStateFlow(false)
    val isDarkTheme: StateFlow<Boolean> = _isDarkTheme

    private val _language = MutableStateFlow("en")
    val language: StateFlow<String> = _language

    val isUkrainian: StateFlow<Boolean> = MutableStateFlow(_language.value == "uk")

    init {
        viewModelScope.launch {
            themePreferenceManager.themeFlow.collect { isDark ->
                _isDarkTheme.value = isDark
            }
        }
        viewModelScope.launch {
            _language.value = languagePreferenceManager.getLanguage()
            (isUkrainian as MutableStateFlow).value = _language.value == "uk"
        }
    }

    fun toggleTheme() {
        viewModelScope.launch {
            themePreferenceManager.saveTheme(!_isDarkTheme.value)
        }
    }

    fun setLanguage(languageCode: String) {
        viewModelScope.launch {
            languagePreferenceManager.setLanguage(languageCode)
            _language.value = languageCode
            (isUkrainian as MutableStateFlow).value = languageCode == "uk"
        }
    }
}