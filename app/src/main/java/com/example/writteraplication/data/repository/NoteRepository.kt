
package com.example.writteraplication.data.repository

import com.example.writteraplication.local.model.NoteDao
import com.example.writteraplication.local.model.NoteEntity
import kotlinx.coroutines.flow.Flow

class NoteRepository(private val noteDao: NoteDao) {

    fun getNotesByProject(projectId: Int): Flow<List<NoteEntity>> {
        return noteDao.getNotesByProject(projectId)
    }

    suspend fun getNoteById(id: Int): NoteEntity? {
        return noteDao.getNoteById(id)
    }

    suspend fun insertNote(note: NoteEntity): Long {
        return noteDao.insertNote(note)
    }

    suspend fun updateNote(note: NoteEntity) {
        noteDao.updateNote(note)
    }

    suspend fun deleteNote(note: NoteEntity) {
        noteDao.deleteNote(note)
    }
}


