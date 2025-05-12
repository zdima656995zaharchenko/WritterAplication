
package com.example.writteraplication.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.writteraplication.R
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
            text = stringResource(R.string.project_editor),
            style = MaterialTheme.typography.headlineSmall
        )

        TextField(
            value = state.title,
            onValueChange = viewModel::onTitleChange,
            label = { Text(stringResource(R.string.project_name)) },
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = state.description,
            onValueChange = viewModel::onDescriptionChange,
            label = { Text(stringResource(R.string.project_description)) },
            modifier = Modifier.fillMaxWidth()
        )

        Button(
            onClick = {
                viewModel.onSaveProject()
            },
            modifier = Modifier.align(Alignment.End)
        ) {
            Text(stringResource(R.string.save_changes))
        }
    }
}
