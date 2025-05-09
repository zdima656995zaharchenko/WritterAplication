
package com.example.writteraplication.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.writteraplication.local.model.TimelineEntity
import com.example.writteraplication.viewmodel.TimelineViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TimelineScreen(
    modifier: Modifier = Modifier,
    timelineViewModel: TimelineViewModel,
    onTimelineClick: (Int) -> Unit, // Додано параметр
    projectId: Int
) {
    val timeline by timelineViewModel.timeline.collectAsState()
    var showDialog by remember { mutableStateOf(false) }

    // 🟢 Завантаження подій при відкритті екрана
    LaunchedEffect(projectId) {
        timelineViewModel.loadTimelineByProject(projectId)
    }

    if (showDialog) {
        AddTimelineDialog(
            onDismiss = { showDialog = false },
            onAdd = { title, description, date ->
                timelineViewModel.addTimeline(
                    title = title,
                    description = description,
                    eventDate = date,
                    projectId = projectId // ← Передано сюди
                )
                showDialog = false
            }
        )
    }

    Scaffold(
        topBar = {
            Column {
                TopAppBar(
                    title = { Text("Хронологія") }
                )

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp),
                    horizontalArrangement = Arrangement.Center
                ) {
                    Button(onClick = { showDialog = true }) {
                        Text("Додати подію")
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
            items(timeline) { event ->
                TimelineItem(
                    event = event,
                    onEdit = { onTimelineClick(event.id) },
                    onDelete = { timelineViewModel.removeTimeline(event) }
                )
            }
        }
    }
}

@Composable
fun TimelineItem(
    event: TimelineEntity,
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
            Text(text = event.title, style = MaterialTheme.typography.titleMedium)
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = event.description, style = MaterialTheme.typography.bodyMedium)
            event.eventDate?.let {
                Spacer(modifier = Modifier.height(4.dp))
                Text(text = "Дата: $it", style = MaterialTheme.typography.labelSmall)
            }
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
fun AddTimelineDialog(
    onDismiss: () -> Unit,
    onAdd: (String, String, String?) -> Unit
) {
    var title by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var date by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Нова подія") },
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
                    maxLines = 3
                )
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = date,
                    onValueChange = { date = it },
                    label = { Text("Дата (необов’язково)") },
                    singleLine = true
                )
            }
        },
        confirmButton = {
            TextButton(onClick = {
                onAdd(title, description, date.ifBlank { null })
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
