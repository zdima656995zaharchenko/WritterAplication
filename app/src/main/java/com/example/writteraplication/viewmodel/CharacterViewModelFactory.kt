package com.example.writteraplication.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.writteraplication.data.repository.CharacterRepository
import com.example.writteraplication.data.repository.FirebaseCharacterRepository

class CharacterViewModelFactory(
    private val repository: CharacterRepository,
    private val firebaseCharacterRepository: FirebaseCharacterRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CharacterViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return CharacterViewModel(repository, firebaseCharacterRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}


