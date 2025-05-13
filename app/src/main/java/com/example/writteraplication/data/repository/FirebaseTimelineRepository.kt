package com.example.writteraplication.data.repository

import android.util.Log
import com.example.writteraplication.local.model.TimelineEntity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class FirebaseTimelineRepository {

    private val db = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()

    private fun getUserTimelinesCollection() = db.collection("users")
        .document(auth.currentUser?.uid ?: "unknown_user")
        .collection("timelines")

    suspend fun saveTimeline(timeline: TimelineEntity) {
        try {
            getUserTimelinesCollection()
                .document(timeline.id.toString())
                .set(timeline)
                .await()
            Log.d("FirebaseTimelineRepo", "✅ Timeline saved successfully")
        } catch (e: Exception) {
            Log.e("FirebaseTimelineRepo", "❌ Error saving timeline", e)
        }
    }

    suspend fun getTimelines(): List<TimelineEntity> {
        return try {
            val snapshot = getUserTimelinesCollection().get().await()
            snapshot.documents.mapNotNull { it.toObject(TimelineEntity::class.java) }
        } catch (e: Exception) {
            Log.e("FirebaseTimelineRepo", "❌ Error fetching timelines", e)
            emptyList()
        }
    }
}
