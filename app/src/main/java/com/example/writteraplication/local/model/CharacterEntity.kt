package com.example.writteraplication.local.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.ColumnInfo

@Entity(tableName = "characters")
data class CharacterEntity(
    @PrimaryKey(autoGenerate = true) var id: Int = 0,
    @ColumnInfo(name = "project_id") var projectId: Int = 0,
    var name: String = "",
    var role: String = "",
    var description: String = "",
    var age: Int = 0,
    var gender: String = "",
    var appearance: String = "",
    var personality: String = "",
    var abilities: String = "",
    var notes: String = ""
) {
    // Порожній конструктор для Firebase
    constructor() : this(0, 0, "", "", "", 0, "", "", "", "", "")
}
