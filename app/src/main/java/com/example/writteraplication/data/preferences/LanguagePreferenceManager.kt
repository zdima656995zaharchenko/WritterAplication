
package com.example.writteraplication.data.preferences

import android.content.Context
import android.content.res.Configuration
import java.util.*

class LanguagePreferenceManager(private val context: Context) {

    companion object {
        private const val PREFS_NAME = "app_prefs"
        private const val KEY_LANGUAGE = "language"
    }

    private val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    fun setLanguage(languageCode: String) {
        prefs.edit().putString(KEY_LANGUAGE, languageCode).apply()
    }

    fun getLanguage(): String {
        return prefs.getString(KEY_LANGUAGE, Locale.getDefault().language) ?: "en"
    }

    fun applyLanguage() {
        val locale = Locale(getLanguage())
        Locale.setDefault(locale)

        val config = Configuration(context.resources.configuration)
        config.setLocale(locale)

        context.resources.updateConfiguration(config, context.resources.displayMetrics)
    }
}


