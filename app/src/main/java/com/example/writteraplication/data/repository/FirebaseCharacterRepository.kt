package com.example.writteraplication.data.repository

import android.util.Log
import com.example.writteraplication.local.model.CharacterEntity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class FirebaseCharacterRepository {
    private val db = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()

    private fun getUserCharactersCollection() =
        db.collection("users")
            .document(auth.currentUser?.uid ?: "unknown_user")
            .collection("characters")

    suspend fun saveCharacter(character: CharacterEntity) {
        try {
            getUserCharactersCollection()
                .document(character.id.toString())
                .set(character)
                .await()
            Log.d("FirebaseCharacterRepo", "✅ Character saved successfully")
        } catch (e: Exception) {
            Log.e("FirebaseCharacterRepo", "❌ Error saving character", e)
        }
    }

    suspend fun getCharacters(): List<CharacterEntity> {
        return try {
            val snapshot = getUserCharactersCollection().get().await()
            snapshot.documents.mapNotNull { it.toObject(CharacterEntity::class.java) }
        } catch (e: Exception) {
            Log.e("FirebaseCharacterRepo", "❌ Error fetching characters", e)
            emptyList()
        }
    }
}
