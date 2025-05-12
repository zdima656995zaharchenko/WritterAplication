package com.example.writteraplication.data.repository

import android.util.Log
import com.example.writteraplication.local.model.NoteEntity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class FirebaseNoteRepository {

    private val db = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()

    private fun getUserNotesCollection() =
        db.collection("users")
            .document(auth.currentUser?.uid ?: "unknown_user")
            .collection("notes")

    suspend fun saveNote(note: NoteEntity) {
        try {
            getUserNotesCollection()
                .document(note.id.toString())
                .set(note)
                .await()
            Log.d("FirebaseNoteRepo", "Note saved successfully")
        } catch (e: Exception) {
            Log.e("FirebaseNoteRepo", "Error saving note", e)
        }
    }

    suspend fun getNotes(): List<NoteEntity> {
        return try {
            val snapshot = getUserNotesCollection().get().await()
            snapshot.documents.mapNotNull { it.toObject(NoteEntity::class.java) }
        } catch (e: Exception) {
            Log.e("FirebaseNoteRepo", "Error fetching notes", e)
            emptyList()
        }
    }
}
