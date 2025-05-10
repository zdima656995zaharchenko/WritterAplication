package com.example.writteraplication.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.writteraplication.data.repository.ProjectRepository
import com.example.writteraplication.local.model.AppDatabase
import com.example.writteraplication.viewmodel.ProjectViewModel
import com.example.writteraplication.viewmodel.ProjectViewModelFactory
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Delete

@Composable
fun FavoritesScreen(navController: NavController, padding: PaddingValues) {
    val context = LocalContext.current
    val db = AppDatabase.getDatabase(context)
    val repository = ProjectRepository(db.projectDao())
    val viewModel: ProjectViewModel = viewModel(
        factory = ProjectViewModelFactory(repository)
    )

    val favoriteProjects = viewModel.projects.filter { it.isFavorite }

    LaunchedEffect(Unit) {
        viewModel.loadProjects()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(padding)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = "Улюблені",
            style = MaterialTheme.typography.headlineSmall
        )

        if (favoriteProjects.isEmpty()) {
            Text("Немає улюблених проєктів.")
        } else {
            LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                items(favoriteProjects) { project ->
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
