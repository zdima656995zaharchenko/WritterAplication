package com.example.writteraplication.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.writteraplication.data.repository.PlotRepository

class PlotsViewModelFactory(
    private val repository: PlotRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(PlotsViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return PlotsViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}


