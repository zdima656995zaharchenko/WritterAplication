
package com.example.writteraplication.local.model

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.writteraplication.local.model.ProjectDao

@Database(
    entities = [PlotEntity::class, CharacterEntity::class, ProjectEntity::class, NoteEntity::class, TimelineEntity::class],
    version = 8,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun plotDao(): PlotDao
    abstract fun characterDao(): CharacterDao
    abstract fun projectDao(): ProjectDao
    abstract fun noteDao(): NoteDao
    abstract fun timelineDao(): TimelineDao


    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "app_database"
                )
                    .fallbackToDestructiveMigration() // Add this line to handle migration
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
