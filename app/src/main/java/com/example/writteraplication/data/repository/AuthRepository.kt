
package com.example.writteraplication.data.repository

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.tasks.await

class AuthRepository(
    private val firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()
) {

    suspend fun registerUser(email: String, password: String): Result<Unit> {
        return try {
            firebaseAuth.createUserWithEmailAndPassword(email, password).await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun loginUser(email: String, password: String): Result<Unit> {
        return try {
            firebaseAuth.signInWithEmailAndPassword(email, password).await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun saveUserName(userId: String, name: String) {
        val db = Firebase.firestore
        db.collection("users").document(userId).set(mapOf("name" to name)).await()
    }

    suspend fun getUserName(userId: String): String? {
        val db = Firebase.firestore
        val document = db.collection("users").document(userId).get().await()
        return document.getString("name")
    }
}
