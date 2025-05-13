
package com.example.writteraplication

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.lifecycle.ViewModelProvider
import com.example.writteraplication.data.preferences.ThemePreferenceManager
import com.example.writteraplication.data.preferences.LanguagePreferenceManager
import com.example.writteraplication.data.repository.PlotRepository
import com.example.writteraplication.data.repository.CharacterRepository
import com.example.writteraplication.data.repository.NoteRepository
import com.example.writteraplication.data.repository.TimelineRepository
import com.example.writteraplication.data.repository.FirebaseNoteRepository
import com.example.writteraplication.data.repository.FirebaseCharacterRepository
import com.example.writteraplication.data.repository.FirebasePlotRepository
import com.example.writteraplication.data.repository.FirebaseProjectRepository
import com.example.writteraplication.local.model.AppDatabase
import com.example.writteraplication.ui.navigation.AppNavigation
import com.example.writteraplication.ui.theme.AppTheme
import com.example.writteraplication.viewmodel.SettingsViewModel
import com.example.writteraplication.viewmodel.SettingsViewModelFactory
import android.content.Context
import android.content.res.Configuration
import com.example.writteraplication.data.repository.FirebaseTimelineRepository
import com.example.writteraplication.data.repository.ProjectRepository
import com.google.firebase.FirebaseApp
import java.util.Locale

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        FirebaseApp.initializeApp(this)

        val database = AppDatabase.getDatabase(applicationContext)
        val projectRepository = ProjectRepository(database.projectDao())
        val plotRepository = PlotRepository(database.plotDao())
        val characterRepository = CharacterRepository(database.characterDao())
        val noteRepository = NoteRepository(database.noteDao())
        val timelineRepository = TimelineRepository(database.timelineDao(), database.characterDao())

        val firebaseRepository = FirebaseNoteRepository()
        val firebaseCharacterRepository = FirebaseCharacterRepository()
        val firebasePlotRepository = FirebasePlotRepository()
        val firebaseTimelineRepository = FirebaseTimelineRepository()
        val firebaseProjectRepository = FirebaseProjectRepository()

        // Create ThemePreferenceManager and LanguagePreferenceManager
        val themeManager = ThemePreferenceManager(applicationContext)
        val languageManager = LanguagePreferenceManager(applicationContext)
        val settingsViewModelFactory = SettingsViewModelFactory(themeManager, languageManager)
        val settingsViewModel = ViewModelProvider(this, settingsViewModelFactory)[SettingsViewModel::class.java]

        setContent {
            AppTheme(settingsViewModel = settingsViewModel){
                AppNavigation(
                    plotRepository = plotRepository,
                    characterRepository = characterRepository,
                    noteRepository = noteRepository,
                    firebaseRepository = firebaseRepository,
                    firebaseCharacterRepository = firebaseCharacterRepository,
                    firebasePlotRepository = firebasePlotRepository,
                    timelineRepository = timelineRepository,
                    firebaseTimelineRepository = firebaseTimelineRepository,
                    projectRepository = projectRepository,
                    firebaseProjectRepository = firebaseProjectRepository,
                    settingsViewModel = settingsViewModel
                )
            }
        }
    }

    override fun attachBaseContext(newBase: Context) {
        val manager = LanguagePreferenceManager(newBase)
        val locale = Locale(manager.getLanguage())
        val config = Configuration()
        config.setLocale(locale)
        val context = newBase.createConfigurationContext(config)
        super.attachBaseContext(context)
    }
}
