
package com.example.writteraplication.viewmodel

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.writteraplication.local.model.PlotEntity
import com.example.writteraplication.data.repository.PlotRepository
import com.example.writteraplication.data.repository.FirebasePlotRepository
import com.example.writteraplication.utils.createPdfFromPlots
import com.example.writteraplication.utils.sendEmailWithAttachment
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import com.google.firebase.auth.FirebaseAuth

class PlotsViewModel(
    private val repository: PlotRepository,
    private val firebasePlotRepository: FirebasePlotRepository
) : ViewModel() {

    private val _plots = MutableStateFlow<List<PlotEntity>>(emptyList())
    val plots: StateFlow<List<PlotEntity>> = _plots.asStateFlow()

    fun getPlotsByProject(projectId: Int): Flow<List<PlotEntity>> {
        return repository.getPlotsByProject(projectId)
    }

    fun addPlot(title: String, description: String, projectId: Int) {
        val newPlot = PlotEntity(
            title = title,
            description = description,
            projectId = projectId
        )
        viewModelScope.launch {
            val result = repository.insert(newPlot)
            Log.d("PlotsViewModel", "✅ Локально збережено. ID: $result")
            firebasePlotRepository.savePlot(newPlot)
            loadPlots(projectId)
        }
    }

    fun removePlot(plotId: Int) {
        viewModelScope.launch {
            val plot = repository.getPlotById(plotId)
            if (plot != null) {
                repository.delete(plot)
                // (опціонально) firebaseRepository.deletePlot(plot)
                loadPlots(plot.projectId)
            }
        }
    }

    fun getPlotById(plotId: Int, onResult: (PlotEntity?) -> Unit) {
        viewModelScope.launch {
            val plot = repository.getPlotById(plotId)
            onResult(plot)
        }
    }

    fun updatePlot(plot: PlotEntity) {
        viewModelScope.launch {
            repository.updatePlot(plot)
            firebasePlotRepository.savePlot(plot)
            loadPlots(plot.projectId)
        }
    }

    fun loadPlots(projectId: Int) {
        viewModelScope.launch {
            repository.getPlotsByProject(projectId)
                .catch { e -> Log.e("PlotsViewModel", "❌ Помилка завантаження", e) }
                .collect { _plots.value = it }
        }
    }

    fun fetchPlotsFromCloud() {
        viewModelScope.launch {
            val cloudPlots = firebasePlotRepository.getPlots()
            _plots.value = cloudPlots
        }
    }

    fun exportPlotsToPdfAndSend(context: Context) {
        viewModelScope.launch {
            try {
                val cloudPlots = firebasePlotRepository.getPlots()
                if (cloudPlots.isNotEmpty()) {
                    val pdfFile = createPdfFromPlots(context, cloudPlots, "PlotsExport")
                    val email = FirebaseAuth.getInstance().currentUser?.email
                    if (email != null) {
                        sendEmailWithAttachment(context, pdfFile, email)
                    } else {
                        Log.w("PlotsViewModel", "❗ Email користувача не знайдено")
                    }
                } else {
                    Log.w("PlotsViewModel", "❗ Немає сюжетів для експорту")
                }
            } catch (e: Exception) {
                Log.e("PlotsViewModel", "❌ Помилка експорту сюжетів", e)
            }
        }
    }
}
