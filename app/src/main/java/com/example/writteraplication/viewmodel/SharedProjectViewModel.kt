package com.example.writteraplication.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

class SharedProjectViewModel : ViewModel() {
    var currentProjectId by mutableStateOf<Int?>(null)
}


