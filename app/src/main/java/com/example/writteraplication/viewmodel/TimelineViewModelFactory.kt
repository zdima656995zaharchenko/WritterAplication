
package com.example.writteraplication.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.writteraplication.data.repository.TimelineRepository
import com.example.writteraplication.data.repository.FirebaseTimelineRepository

class TimelineViewModelFactory(
    private val repository: TimelineRepository,
    private val firebaseTimelineRepository: FirebaseTimelineRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(TimelineViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return TimelineViewModel(repository, firebaseTimelineRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
