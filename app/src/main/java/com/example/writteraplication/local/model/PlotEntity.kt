package com.example.writteraplication.local.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.ColumnInfo

@Entity(tableName = "plots")
data class PlotEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    @ColumnInfo(name = "project_id") val projectId: Int = 0,
    val title: String = "",
    val description: String = "",
    val eventDate: String? = null,
    val cause: String? = null,
    val consequence: String? = null,
    val relatedCharacters: String? = null
)

