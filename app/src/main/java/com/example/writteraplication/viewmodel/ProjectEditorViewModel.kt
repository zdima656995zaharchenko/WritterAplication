
package com.example.writteraplication.viewmodel

import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.writteraplication.data.repository.ProjectRepository
import com.example.writteraplication.local.model.ProjectEntity
import kotlinx.coroutines.launch

data class ProjectUiState(
    val title: String = "",
    val description: String = "",
    val isSaved: Boolean = false,
    val projectId: Int? = null
)

class ProjectEditorViewModel(private val repository: ProjectRepository) : ViewModel() {
    var uiState by mutableStateOf(ProjectUiState())
        private set

    fun onTitleChange(newTitle: String) {
        uiState = uiState.copy(title = newTitle)
    }

    fun onDescriptionChange(newDesc: String) {
        uiState = uiState.copy(description = newDesc)
    }

    fun onSaveProject() {
        viewModelScope.launch {
            val project = ProjectEntity(
                name = uiState.title,
                description = uiState.description
            )
            val projectId = repository.insertProject(project)
            uiState = uiState.copy(isSaved = true, projectId = projectId.toInt())
        }
    }

    fun resetSaveFlag() {
        uiState = uiState.copy(isSaved = false)
    }
}
