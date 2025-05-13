
package com.example.writteraplication.ui.screens

import androidx.compose.ui.platform.LocalContext
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
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
import com.example.writteraplication.data.repository.FirebaseProjectRepository
import androidx.compose.ui.Alignment
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Delete
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.asPaddingValues

@Composable
fun ProjectsScreen(
    navController: NavController,
    padding: PaddingValues,
    firebaseProjectRepository: FirebaseProjectRepository
) {
    val context = LocalContext.current
    val db = AppDatabase.getDatabase(context)
    val repository = ProjectRepository(db.projectDao())
    val viewModel: ProjectViewModel = viewModel(
        factory = ProjectViewModelFactory(repository, firebaseProjectRepository)
    )
    val projects = viewModel.projects

    // Завантаження проєктів при першому запуску
    LaunchedEffect(Unit) {
        viewModel.loadProjects()
    }

    Scaffold(
        bottomBar = {
            Button(
                onClick = { viewModel.fetchProjectsFromCloud() },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Text("Синхронізувати з хмарою")
            }
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 16.dp)
                .padding(WindowInsets.statusBars.asPaddingValues()), // Додаємо відступ для статус-бара
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 16.dp)
                ) {
                    Text(
                        text = "Плоєкти",
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
                }
            }

            if (projects.isEmpty()) {
                item {
                    Text("Немає збережених проєктів.")
                }
            } else {
                items(projects) { project ->
                    Card(
                        onClick = {
                            navController.navigate("project_content/${project.id}")
                        },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text(text = project.name, style = MaterialTheme.typography.titleMedium)
                                Text(text = project.description, style = MaterialTheme.typography.bodyMedium)
                            }

                            Row {
                                IconButton(onClick = { viewModel.toggleFavorite(project) }) {
                                    Icon(
                                        imageVector = if (project.isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                                        contentDescription = "Улюблене"
                                    )
                                }

                                IconButton(onClick = { viewModel.deleteProject(project) }) {
                                    Icon(
                                        imageVector = Icons.Default.Delete,
                                        contentDescription = "Видалити"
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
