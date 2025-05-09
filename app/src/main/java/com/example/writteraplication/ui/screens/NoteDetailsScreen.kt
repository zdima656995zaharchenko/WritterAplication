
package com.example.writteraplication.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.writteraplication.viewmodel.NoteViewModel
import com.example.writteraplication.local.model.NoteEntity

@Composable
fun NoteDetailsScreen(
    noteId: Int,
    projectId: Int,
    noteViewModel: NoteViewModel,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    var note by remember { mutableStateOf<NoteEntity?>(null) }
    var title by remember { mutableStateOf("") }
    var content by remember { mutableStateOf("") }

    LaunchedEffect(noteId) {
        if (noteId != -1) {
            val loadedNote = noteViewModel.getNoteById(noteId)
            note = loadedNote
            loadedNote?.let {
                title = it.title
                content = it.content
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
            text = if (noteId == -1) "Нова нотатка" else "Деталі нотатки",
            style = MaterialTheme.typography.headlineSmall,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        OutlinedTextField(value = title, onValueChange = { title = it }, label = { Text("Назва") }, modifier = Modifier.fillMaxWidth())
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(value = content, onValueChange = { content = it }, label = { Text("Текст") }, modifier = Modifier.fillMaxWidth())
        Spacer(modifier = Modifier.height(16.dp))

        Row {
            Button(
                onClick = {
                    if (noteId == -1) {
                        noteViewModel.addNote(
                            title = title,
                            content = content,
                            projectId = projectId
                        )
                    } else {
                        val updatedNote = note!!.copy(
                            title = title,
                            content = content,
                            timestamp = System.currentTimeMillis()
                        )
                        noteViewModel.updateNote(updatedNote)
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


