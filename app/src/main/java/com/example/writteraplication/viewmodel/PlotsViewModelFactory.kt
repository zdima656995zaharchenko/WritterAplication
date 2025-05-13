
package com.example.writteraplication.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.writteraplication.data.repository.PlotRepository
import com.example.writteraplication.data.repository.FirebasePlotRepository

class PlotsViewModelFactory(
    private val repository: PlotRepository,
    private val firebasePlotRepository: FirebasePlotRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(PlotsViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return PlotsViewModel(repository, firebasePlotRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
