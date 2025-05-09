package com.example.writteraplication

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.writteraplication.data.repository.PlotRepository
import com.example.writteraplication.data.repository.CharacterRepository
import com.example.writteraplication.data.repository.NoteRepository
import com.example.writteraplication.data.repository.TimelineRepository
import com.example.writteraplication.local.model.AppDatabase
import com.example.writteraplication.ui.navigation.AppNavigation
import com.example.writteraplication.ui.theme.WritterApplicationTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Підключення до об'єднаної бази даних
        val database = AppDatabase.getDatabase(applicationContext)

        // Створення репозиторіїв
        val plotRepository = PlotRepository(database.plotDao())
        val characterRepository = CharacterRepository(database.characterDao())
        val noteRepository = NoteRepository(database.noteDao())
        val timelineRepository = TimelineRepository(
            database.timelineDao(),
            database.characterDao()
        )


        setContent {
            WritterApplicationTheme {
                AppNavigation(
                    plotRepository = plotRepository,
                    characterRepository = characterRepository,
                    noteRepository = noteRepository,
                    timelineRepository = timelineRepository
                )
            }
        }
    }
}
