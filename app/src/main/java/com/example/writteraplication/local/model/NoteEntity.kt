
package com.example.writteraplication.local.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.ColumnInfo

@Entity(tableName = "notes")
data class NoteEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    @ColumnInfo(name = "project_id") val projectId: Int, // 🔗 зв'язок з проєктом
    val title: String,
    val content: String,
    val timestamp: Long
)


