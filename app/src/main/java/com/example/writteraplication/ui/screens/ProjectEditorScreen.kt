
package com.example.writteraplication.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.writteraplication.local.model.AppDatabase
import com.example.writteraplication.data.repository.ProjectRepository
import com.example.writteraplication.viewmodel.ProjectEditorViewModel
import com.example.writteraplication.viewmodel.ProjectEditorViewModelFactory

@Composable
fun ProjectEditorScreen(
    navController: NavController,
    padding: PaddingValues
) {
    val context = LocalContext.current
    val db = AppDatabase.getDatabase(context)
    val repository = ProjectRepository(db.projectDao())
    val viewModel: ProjectEditorViewModel = viewModel(
        factory = ProjectEditorViewModelFactory(repository)
    )

    val state = viewModel.uiState

    // Якщо проєкт збережено — переходимо до наступного екрану
    LaunchedEffect(state.isSaved) {
        if (state.isSaved) {
            val projectId = state.projectId
            navController.navigate("project_content/$projectId")
            viewModel.resetSaveFlag()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(padding)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = "Редактор проєкту",
            style = MaterialTheme.typography.headlineSmall
        )

        TextField(
            value = state.title,
            onValueChange = viewModel::onTitleChange,
            label = { Text("Назва проєкту") },
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = state.description,
            onValueChange = viewModel::onDescriptionChange,
            label = { Text("Опис проєкту") },
            modifier = Modifier.fillMaxWidth()
        )

        Button(
            onClick = {
                viewModel.onSaveProject()
            },
            modifier = Modifier.align(Alignment.End)
        ) {
            Text("Зберегти зміни")
        }
    }
}
