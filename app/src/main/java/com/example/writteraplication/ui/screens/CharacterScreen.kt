
package com.example.writteraplication.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.writteraplication.local.model.CharacterEntity
import com.example.writteraplication.viewmodel.CharacterViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CharacterScreen(
    modifier: Modifier = Modifier,
    characterViewModel: CharacterViewModel,
    onCharacterClick: (Int) -> Unit, // ← Додано параметр
    projectId: Int
) {
    val characters by characterViewModel.characters.collectAsState()
    var showDialog by remember { mutableStateOf(false) }

    // 🟢 Завантаження персонажів при відкритті екрана
    LaunchedEffect(projectId) {
        characterViewModel.loadCharactersByProject(projectId)
    }

    if (showDialog) {
        AddCharacterDialog(
            onDismiss = { showDialog = false },
            onAdd = { name, role, description ->
                characterViewModel.addCharacter(
                    name = name,
                    role = role,
                    description = description,
                    age = 0,
                    gender = "",
                    appearance = "",
                    personality = "",
                    abilities = "",
                    notes = "",
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
                    title = { Text("Персонажі") }
                )

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp),
                    horizontalArrangement = Arrangement.Center
                ) {
                    Button(onClick = { showDialog = true }) {
                        Text("Додати персонажа")
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
            items(characters) { character ->
                CharacterItem(
                    character = character,
                    onEdit = { onCharacterClick(character.id) },
                    onDelete = { characterViewModel.removeCharacter(character) }
                )
            }
        }
    }
}

@Composable
fun CharacterItem(
    character: CharacterEntity,
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
            Text(text = character.name, style = MaterialTheme.typography.titleMedium)
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = character.role, style = MaterialTheme.typography.bodyMedium)
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
fun AddCharacterDialog(
    onDismiss: () -> Unit,
    onAdd: (String, String, String) -> Unit
) {
    var name by remember { mutableStateOf("") }
    var role by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Новий персонаж") },
        text = {
            Column {
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Ім'я") },
                    singleLine = true
                )
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = role,
                    onValueChange = { role = it },
                    label = { Text("Роль") },
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
                onAdd(name, role, description)
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
