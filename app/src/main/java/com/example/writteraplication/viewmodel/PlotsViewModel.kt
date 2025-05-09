
package com.example.writteraplication.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.writteraplication.local.model.PlotEntity
import com.example.writteraplication.data.repository.PlotRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class PlotsViewModel(private val repository: PlotRepository) : ViewModel() {

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
            repository.insert(newPlot) // Використовуємо правильну назву методу
        }
    }

    fun removePlot(plotId: Int) {
        viewModelScope.launch {
            val plot = repository.getPlotById(plotId)
            if (plot != null) {
                repository.delete(plot) // Використовуємо правильну назву методу
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
        }
    }
}
