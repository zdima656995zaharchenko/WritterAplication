
package com.example.writteraplication.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.writteraplication.viewmodel.TimelineViewModel
import com.example.writteraplication.local.model.TimelineEntity

@Composable
fun TimelineDetailsScreen(
    id: Int,
    projectId: Int,
    timelineViewModel: TimelineViewModel,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    var event by remember { mutableStateOf<TimelineEntity?>(null) }
    var title by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var eventDate by remember { mutableStateOf("") }

    LaunchedEffect(id) {
        if (id != -1) {
            val loadedEvent = timelineViewModel.getTimelineById(id)
            event = loadedEvent
            loadedEvent?.let {
                title = it.title
                description = it.description
                eventDate = it.eventDate.orEmpty()
            }
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        Text(
            text = if (id == -1) "Нова подія" else "Деталі події",
            style = MaterialTheme.typography.headlineSmall,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        OutlinedTextField(value = title, onValueChange = { title = it }, label = { Text("Назва") }, modifier = Modifier.fillMaxWidth())
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(value = description, onValueChange = { description = it }, label = { Text("Опис") }, modifier = Modifier.fillMaxWidth())
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(value = eventDate, onValueChange = { eventDate = it }, label = { Text("Дата події (опціонально)") }, modifier = Modifier.fillMaxWidth())
        Spacer(modifier = Modifier.height(16.dp))

        Row {
            Button(
                onClick = {
                    if (id == -1) {
                        timelineViewModel.addTimeline(
                            title = title,
                            description = description,
                            eventDate = eventDate,
                            projectId = projectId
                        )
                    } else {
                        val updatedEvent = event!!.copy(
                            title = title,
                            description = description,
                            eventDate = eventDate.ifBlank { null },
                            projectId = projectId
                        )
                        timelineViewModel.updateTimeline(updatedEvent)
                    }
                    onBack()
                },
                modifier = Modifier.weight(1f)
            ) {
                Text("Зберегти")
            }

            Spacer(modifier = Modifier.width(8.dp))

            OutlinedButton(
                onClick = onBack,
                modifier = Modifier.weight(1f)
            ) {
                Text("Назад")
            }
        }
    }
}
