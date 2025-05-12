
package com.example.writteraplication.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.writteraplication.data.repository.FirebaseNoteRepository
import com.example.writteraplication.data.repository.NoteRepository

class NoteViewModelFactory(
    private val localRepository: NoteRepository,
    private val firebaseRepository: FirebaseNoteRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(NoteViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return NoteViewModel(localRepository, firebaseRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}


