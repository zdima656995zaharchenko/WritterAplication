
package com.example.writteraplication

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.lifecycle.ViewModelProvider
import com.example.writteraplication.data.preferences.ThemePreferenceManager
import com.example.writteraplication.data.repository.PlotRepository
import com.example.writteraplication.data.repository.CharacterRepository
import com.example.writteraplication.data.repository.NoteRepository
import com.example.writteraplication.data.repository.TimelineRepository
import com.example.writteraplication.local.model.AppDatabase
import com.example.writteraplication.ui.navigation.AppNavigation
import com.example.writteraplication.ui.theme.AppTheme
import com.example.writteraplication.viewmodel.SettingsViewModel
import com.example.writteraplication.viewmodel.SettingsViewModelFactory

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val database = AppDatabase.getDatabase(applicationContext)
        val plotRepository = PlotRepository(database.plotDao())
        val characterRepository = CharacterRepository(database.characterDao())
        val noteRepository = NoteRepository(database.noteDao())
        val timelineRepository = TimelineRepository(database.timelineDao(), database.characterDao())

        // Create ThemePreferenceManager and SettingsViewModel
        val themeManager = ThemePreferenceManager(applicationContext)
        val settingsViewModelFactory = SettingsViewModelFactory(themeManager)
        val settingsViewModel = ViewModelProvider(this, settingsViewModelFactory)[SettingsViewModel::class.java]

        setContent {
            AppTheme(settingsViewModel = settingsViewModel){
                AppNavigation(
                    plotRepository = plotRepository,
                    characterRepository = characterRepository,
                    noteRepository = noteRepository,
                    timelineRepository = timelineRepository,
                    settingsViewModel = settingsViewModel
                )
            }
        }
    }
}
