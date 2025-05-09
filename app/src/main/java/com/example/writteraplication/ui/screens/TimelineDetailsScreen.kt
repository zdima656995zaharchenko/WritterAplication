
package com.example.writteraplication.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDownward
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.writteraplication.local.model.TimelineEntity
import com.example.writteraplication.viewmodel.TimelineViewModel

@Composable
@OptIn(ExperimentalMaterial3Api::class)
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
    val eventCharacters = remember { mutableStateListOf("") }

    LaunchedEffect(id) {
        if (id != -1) {
            val loadedEvent = timelineViewModel.getTimelineById(id)
            event = loadedEvent
            loadedEvent?.let {
                title = it.title
                description = it.description
                eventDate = it.eventDate.orEmpty()
                eventCharacters.clear()
                eventCharacters.addAll(it.characters)
            }
        }
    }

    LaunchedEffect(projectId) {
        timelineViewModel.loadCharactersByProject(projectId)
    }

    val eventElements by timelineViewModel.eventElements.collectAsState()
    val allCharacters by timelineViewModel.characters.collectAsState()

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

        Text("Елементи події", style = MaterialTheme.typography.titleMedium)
        eventElements.forEachIndexed { index, element ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    OutlinedTextField(
                        value = element.title,
                        onValueChange = { newTitle -> timelineViewModel.updateEventElement(element.id, newTitle, element.date, element.characters) },
                        label = { Text("Назва елемента") }
                    )
                    OutlinedTextField(
                        value = element.date,
                        onValueChange = { newDate -> timelineViewModel.updateEventElement(element.id, element.title, newDate, element.characters) },
                        label = { Text("Дата") }
                    )
                    // Вибір персонажів
                    var expanded by remember { mutableStateOf(false) }
                    val selectedCharacters = remember { mutableStateListOf<String>() }
                    ExposedDropdownMenuBox(
                        expanded = expanded,
                        onExpandedChange = { expanded = !expanded }
                    ) {
                        OutlinedTextField(
                            value = element.characters.joinToString(", "),
                            onValueChange = { newCharacters -> timelineViewModel.updateEventElement(element.id, element.title, element.date, newCharacters.split(", ")) },
                            readOnly = true,
                            label = { Text("Персонажі") },
                            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                            modifier = Modifier.menuAnchor()
                        )
                        ExposedDropdownMenu(
                            expanded = expanded,
                            onDismissRequest = { expanded = false }
                        ) {
                            allCharacters.forEach { character ->
                                DropdownMenuItem(
                                    text = { Text(character.name) },
                                    onClick = {
                                        if (selectedCharacters.contains(character.name)) {
                                            selectedCharacters.remove(character.name)
                                        } else {
                                            selectedCharacters.add(character.name)
                                        }
                                        timelineViewModel.updateEventElement(element.id, element.title, element.date, selectedCharacters)
                                        expanded = false
                                    }
                                )
                            }
                        }
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.End
                    ) {
                        OutlinedButton(
                            onClick = { timelineViewModel.removeEventElement(element.id) },
                            modifier = Modifier.padding(end = 8.dp)
                        ) {
                            Text("Видалити")
                        }

                    }
                }
            }

            if (index < eventElements.lastIndex) {
                Icon(
                    imageVector = Icons.Default.ArrowDownward,
                    contentDescription = "Стрілка вниз",
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                        .padding(4.dp)
                )
            }
        }

        Button(onClick = { timelineViewModel.addEventElement() }) {
            Text("Додати елемент події")
        }

        Spacer(modifier = Modifier.height(16.dp))

        Row {
            Button(
                onClick = {
                    if (id == -1) {
                        timelineViewModel.addTimeline(
                            title = title,
                            description = description,
                            eventDate = eventDate,
                            projectId = projectId,
                            characters = eventCharacters
                        )
                    } else {
                        val updatedEvent = event!!.copy(
                            title = title,
                            description = description,
                            eventDate = eventDate.ifBlank { null },
                            projectId = projectId,
                            characters = eventCharacters
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



