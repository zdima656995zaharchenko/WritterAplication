
package com.example.writteraplication.viewmodel

data class EventElement(
    val id: String,
    val title: String,
    val date: String = "",
    val description: String = "",
    val characters: List<String> = emptyList()
)
