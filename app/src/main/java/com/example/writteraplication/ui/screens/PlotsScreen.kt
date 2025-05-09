
package com.example.writteraplication.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.writteraplication.local.model.PlotEntity
import com.example.writteraplication.viewmodel.PlotsViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PlotScreen(
    modifier: Modifier = Modifier,
    plotsViewModel: PlotsViewModel,
    projectId: Int, // ← Додано параметр
    onPlotClick: (Int) -> Unit
) {
    val plots by plotsViewModel.getPlotsByProject(projectId).collectAsState(initial = emptyList()) // ← Використовуємо getPlotsByProject
    var showDialog by remember { mutableStateOf(false) }

    if (showDialog) {
        AddPlotDialog(
            onDismiss = { showDialog = false },
            onAdd = { title, description ->
                plotsViewModel.addPlot(title, description, projectId) // ← Передаємо projectId
                showDialog = false
            }
        )
    }

    Scaffold(
        topBar = {
            Column {
                TopAppBar(
                    title = { Text("Сюжети") }
                )
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp),
                    horizontalArrangement = Arrangement.Center
                ) {
                    Button(onClick = { showDialog = true }) {
                        Text("Додати сюжет")
                    }
                }
            }
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            items(plots) { plot ->
                PlotItem(
                    plot = plot,
                    onEdit = { onPlotClick(plot.id) }, // ← Передаємо plot.id
                    onDelete = { plotsViewModel.removePlot(plot.id) } // ← Передаємо plot.id
                )
            }
        }
    }
}

@Composable
fun PlotItem(
    plot: PlotEntity,
    onEdit: () -> Unit,
    onDelete: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = plot.title, style = MaterialTheme.typography.titleMedium)
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = plot.description, style = MaterialTheme.typography.bodyMedium)
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                horizontalArrangement = Arrangement.End,
                modifier = Modifier.fillMaxWidth()
            ) {
                OutlinedButton(onClick = onEdit) {
                    Text("Редагувати")
                }
                Spacer(modifier = Modifier.width(8.dp))
                Button(onClick = onDelete) {
                    Text("Видалити")
                }
            }
        }
    }
}

@Composable
fun AddPlotDialog(
    onDismiss: () -> Unit,
    onAdd: (String, String) -> Unit
) {
    var title by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Новий сюжет") },
        text = {
            Column {
                OutlinedTextField(
                    value = title,
                    onValueChange = { title = it },
                    label = { Text("Назва") },
                    singleLine = true
                )
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = description,
                    onValueChange = { description = it },
                    label = { Text("Опис") },
                    maxLines = 4
                )
            }
        },
        confirmButton = {
            TextButton(onClick = {
                onAdd(title, description)
            }) {
                Text("Додати")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Скасувати")
            }
        }
    )
}
