
package com.example.writteraplication.viewmodel

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.writteraplication.local.model.ProjectEntity
import com.example.writteraplication.data.repository.ProjectRepository
import com.example.writteraplication.data.repository.FirebaseProjectRepository
import kotlinx.coroutines.launch

class ProjectViewModel(
    private val repository: ProjectRepository,
    private val firebaseProjectRepository: FirebaseProjectRepository
) : ViewModel() {

    val projects = mutableStateListOf<ProjectEntity>()
    val favoriteProjects = mutableStateListOf<ProjectEntity>()

    fun loadProjects() {
        viewModelScope.launch {
            projects.clear()
            projects.addAll(repository.getAllProjects())
        }
    }

    fun insertProject(project: ProjectEntity) {
        viewModelScope.launch {
            repository.insertProject(project)
            firebaseProjectRepository.saveProject(project)
            loadProjects()
        }
    }

    fun updateProject(project: ProjectEntity) {
        viewModelScope.launch {
            repository.updateProject(project)
            firebaseProjectRepository.saveProject(project)
            loadProjects()
        }
    }

    fun fetchProjectsFromCloud() {
        viewModelScope.launch {
            val cloudProjects = firebaseProjectRepository.getProjects()

            // Очистити локальну базу (опціонально, якщо потрібно повністю замінити)
            projects.clear()

            // Зберегти кожен проєкт у локальну базу
            cloudProjects.forEach { project ->
                repository.insertProject(project)
            }

            // Завантажити з локальної бази
            loadProjects()
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
