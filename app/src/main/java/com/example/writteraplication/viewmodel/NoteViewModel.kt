
package com.example.writteraplication.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.writteraplication.local.model.NoteEntity
import com.example.writteraplication.data.repository.NoteRepository
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class NoteViewModel(private val repository: NoteRepository) : ViewModel() {

    private val _notes = MutableStateFlow<List<NoteEntity>>(emptyList())
    val notes: StateFlow<List<NoteEntity>> = _notes.asStateFlow()

    fun loadNotesByProject(projectId: Int) {
        viewModelScope.launch {
            repository.getNotesByProject(projectId)
                .catch { e -> Log.e("NoteViewModel", "Помилка завантаження нотаток", e) }
                .collect { notes ->
                    _notes.value = notes
                }
        }
    }

    fun addNote(
        title: String,
        content: String,
        projectId: Int
    ) {
        viewModelScope.launch {
            Log.d("NoteViewModel", "🟡 Додаємо нотатку: $title ($projectId)")
            val note = NoteEntity(
                title = title,
                content = content,
                timestamp = System.currentTimeMillis(),
                projectId = projectId
            )
            val result = repository.insertNote(note)
            Log.d("NoteViewModel", "✅ Вставка завершена. ID нової нотатки: $result")

            // Оновити список після вставки
            loadNotesByProject(projectId)
        }
    }

    fun updateNote(note: NoteEntity) {
        viewModelScope.launch {
            repository.updateNote(note)
            loadNotesByProject(note.projectId)
        }
    }

    fun removeNote(note: NoteEntity) {
        viewModelScope.launch {
            repository.deleteNote(note)
            loadNotesByProject(note.projectId)
        }
    }

    suspend fun getNoteById(id: Int): NoteEntity? {
        return repository.getNoteById(id)
    }
}


