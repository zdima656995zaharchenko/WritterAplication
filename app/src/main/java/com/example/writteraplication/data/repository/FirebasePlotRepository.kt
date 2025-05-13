package com.example.writteraplication.data.repository

import android.util.Log
import com.example.writteraplication.local.model.PlotEntity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class FirebasePlotRepository {

    private val db = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()

    private fun getUserPlotsCollection() = db.collection("users")
        .document(auth.currentUser?.uid ?: "unknown_user")
        .collection("plots")

    suspend fun savePlot(plot: PlotEntity) {
        try {
            getUserPlotsCollection()
                .document(plot.id.toString())
                .set(plot)
                .await()
            Log.d("FirebasePlotRepo", "✅ Plot saved successfully")
        } catch (e: Exception) {
            Log.e("FirebasePlotRepo", "❌ Error saving plot", e)
        }
    }

    suspend fun getPlots(): List<PlotEntity> {
        return try {
            val snapshot = getUserPlotsCollection().get().await()
            snapshot.documents.mapNotNull { it.toObject(PlotEntity::class.java) }
        } catch (e: Exception) {
            Log.e("FirebasePlotRepo", "❌ Error fetching plots", e)
            emptyList()
        }
    }
}
