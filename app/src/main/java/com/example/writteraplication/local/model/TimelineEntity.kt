package com.example.writteraplication.local.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.ColumnInfo

@Entity(tableName = "timeline")
data class TimelineEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    @ColumnInfo(name = "project_id") val projectId: Int,
    val title: String,
    val description: String,
    val eventDate: String? = null
)



