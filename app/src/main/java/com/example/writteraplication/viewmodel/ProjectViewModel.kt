package com.example.writteraplication.viewmodel

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.writteraplication.local.model.ProjectEntity
import com.example.writteraplication.data.repository.ProjectRepository
import kotlinx.coroutines.launch

class ProjectViewModel(private val repository: ProjectRepository) : ViewModel() {
    val projects = mutableStateListOf<ProjectEntity>()

    fun insertProject(project: ProjectEntity) {
        viewModelScope.launch {
            repository.insertProject(project)
            projects.add(project)
        }
    }

    fun getAllProjects() {
        viewModelScope.launch {
            projects.clear()
            projects.addAll(repository.getAllProjects())
        }
    }

    fun getProjectById(id: Int): ProjectEntity? {
        var project: ProjectEntity? = null
        viewModelScope.launch {
            project = repository.getProjectById(id)
        }
        return project
    }

    fun deleteProject(project: ProjectEntity) {
        viewModelScope.launch {
            repository.deleteProject(project)
            projects.remove(project)
        }
    }
}


