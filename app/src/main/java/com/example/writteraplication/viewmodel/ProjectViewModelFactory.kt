
package com.example.writteraplication.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.writteraplication.data.repository.ProjectRepository
import com.example.writteraplication.data.repository.FirebaseProjectRepository

class ProjectViewModelFactory(
    private val repository: ProjectRepository,
    private val firebaseProjectRepository: FirebaseProjectRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ProjectViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ProjectViewModel(repository, firebaseProjectRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
