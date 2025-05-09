
package com.example.writteraplication.ui.screens


import androidx.compose.ui.platform.LocalContext
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.writteraplication.local.model.AppDatabase
import com.example.writteraplication.data.repository.ProjectRepository
import com.example.writteraplication.viewmodel.ProjectViewModel
import com.example.writteraplication.viewmodel.ProjectViewModelFactory

@Composable
fun ProjectsScreen(navController: NavController, padding: PaddingValues) {
    val context = LocalContext.current
    val db = AppDatabase.getDatabase(context)
    val repository = ProjectRepository(db.projectDao())
    val viewModel: ProjectViewModel = viewModel(
        factory = ProjectViewModelFactory(repository)
    )
    val projects = viewModel.projects

    // Завантаження проєктів при першому запуску
    LaunchedEffect(Unit) {
        viewModel.getAllProjects()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(padding)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = "Проєкти",
            style = MaterialTheme.typography.headlineSmall
        )

        Button(
            onClick = {
                navController.navigate("project_editor")
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Створити новий проєкт")
        }

        Spacer(modifier = Modifier.height(16.dp))

        if (projects.isEmpty()) {
            Text("Немає збережених проєктів.")
        } else {
            LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                items(projects) { project ->
                    Card(
                        onClick = {
                            navController.navigate("project_content/${project.id}")
                        },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(text = project.name, style = MaterialTheme.typography.titleMedium)
                            Text(text = project.description, style = MaterialTheme.typography.bodyMedium)
                        }
                    }
                }
            }
        }
    }
}
