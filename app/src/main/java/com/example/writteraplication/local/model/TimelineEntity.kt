
package com.example.writteraplication.local.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.ColumnInfo

@Entity(tableName = "timeline")
data class TimelineEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    @ColumnInfo(name = "project_id") val projectId: Int = 0,
    val title: String = "",
    val description: String = "",
    val eventDate: String? = null,
    @ColumnInfo(name = "characters") val characters: List<String> = emptyList() // Додано поле characters
) {
    // Порожній конструктор для Firebase
    constructor() : this(0, 0, "", "", null, emptyList())
}
