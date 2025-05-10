package com.example.writteraplication.viewmodel

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.writteraplication.local.model.ProjectEntity
import com.example.writteraplication.data.repository.ProjectRepository
import kotlinx.coroutines.launch

class ProjectViewModel(private val repository: ProjectRepository) : ViewModel() {

    val projects = mutableStateListOf<ProjectEntity>()
    val favoriteProjects = mutableStateListOf<ProjectEntity>()

    fun loadProjects() {
        viewModelScope.launch {
            projects.clear()
            projects.addAll(repository.getAllProjects())
        }
    }

    fun getFavoriteProjects() {
        viewModelScope.launch {
            favoriteProjects.clear()
            favoriteProjects.addAll(repository.getFavoriteProjects())
        }
    }

    fun getProjectById(id: Int, onResult: (ProjectEntity?) -> Unit) {
        viewModelScope.launch {
            val project = repository.getProjectById(id)
            onResult(project)
        }
    }

    fun toggleFavorite(project: ProjectEntity) {
        viewModelScope.launch {
            val updatedProject = project.copy(isFavorite = !project.isFavorite)
            repository.updateProject(updatedProject)
            loadProjects()
            getFavoriteProjects()
        }
    }

    fun deleteProject(project: ProjectEntity) {
        viewModelScope.launch {
            repository.deleteProject(project)
            loadProjects()
            getFavoriteProjects()
        }
    }
}




