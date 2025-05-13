package com.example.writteraplication.data.repository

import android.util.Log
import com.example.writteraplication.local.model.ProjectEntity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class FirebaseProjectRepository {
    private val db = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()

    private fun getUserProjectsCollection() =
        db.collection("users")
            .document(auth.currentUser?.uid ?: "unknown_user")
            .collection("projects")

    suspend fun saveProject(project: ProjectEntity) {
        try {
            getUserProjectsCollection()
                .document(project.id.toString())
                .set(project)
                .await()
            Log.d("FirebaseProjectRepo", "✅ Project saved successfully")
        } catch (e: Exception) {
            Log.e("FirebaseProjectRepo", "❌ Error saving project", e)
        }
    }

    suspend fun getProjects(): List<ProjectEntity> {
        return try {
            val snapshot = getUserProjectsCollection().get().await()
            snapshot.documents.mapNotNull { it.toObject(ProjectEntity::class.java) }
        } catch (e: Exception) {
            Log.e("FirebaseProjectRepo", "❌ Error fetching projects", e)
            emptyList()
        }
    }
}
