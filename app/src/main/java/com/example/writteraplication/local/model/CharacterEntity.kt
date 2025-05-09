package com.example.writteraplication.local.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.ColumnInfo

@Entity(tableName = "characters")
data class CharacterEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    @ColumnInfo(name = "project_id") val projectId: Int, // 🔗 зв'язок з проєктом
    val name: String,
    val role: String,
    val description: String,
    val age: Int,
    val gender: String,
    val appearance: String,
    val personality: String,
    val abilities: String,
    val notes: String
)
