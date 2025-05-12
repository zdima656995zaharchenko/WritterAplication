
package com.example.writteraplication.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.writteraplication.local.model.NoteEntity
import com.example.writteraplication.data.repository.NoteRepository
import com.example.writteraplication.data.repository.FirebaseNoteRepository
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class NoteViewModel(
    private val repository: NoteRepository,
    private val firebaseRepository: FirebaseNoteRepository
) : ViewModel() {

    private val _notes = MutableStateFlow<List<NoteEntity>>(emptyList())
    val notes: StateFlow<List<NoteEntity>> = _notes.asStateFlow()

    fun loadNotesByProject(projectId: Int) {
        viewModelScope.launch {
            repository.getNotesByProject(projectId)
                .catch { e -> Log.e("NoteViewModel", "Помилка завантаження нотаток", e) }
                .collect { notes -> _notes.value = notes }
        }
    }

    fun addNote(title: String, content: String, projectId: Int) {
        viewModelScope.launch {
            val note = NoteEntity(
                title = title,
                content = content,
                timestamp = System.currentTimeMillis(),
                projectId = projectId
            )
            val result = repository.insertNote(note)
            Log.d("NoteViewModel", "✅ Локально збережено. ID: $result")

            // Синхронізуємо в хмару
            firebaseRepository.saveNote(note)

            loadNotesByProject(projectId)
        }
    }

    fun updateNote(note: NoteEntity) {
        viewModelScope.launch {
            repository.updateNote(note)
            firebaseRepository.saveNote(note) // оновлюємо і в хмарі
            loadNotesByProject(note.projectId)
        }
    }

    fun removeNote(note: NoteEntity) {
        viewModelScope.launch {
            repository.deleteNote(note)
            // (опціонально) firebaseRepository.deleteNote(note)
            loadNotesByProject(note.projectId)
        }
    }

    suspend fun getNoteById(id: Int): NoteEntity? {
        return repository.getNoteById(id)
    }

    fun fetchNotesFromCloud() {
        viewModelScope.launch {
            val cloudNotes = firebaseRepository.getNotes()
            _notes.value = cloudNotes
        }
    }
}


