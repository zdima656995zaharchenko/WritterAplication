package com.example.writteraplication.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.ui.platform.LocalContext
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.writteraplication.viewmodel.CharacterViewModel
import com.example.writteraplication.local.model.CharacterEntity
import kotlinx.coroutines.launch

@Composable
fun CharacterDetailsScreen(
    characterId: Int,
    projectId: Int,
    characterViewModel: CharacterViewModel,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    var character by remember { mutableStateOf<CharacterEntity?>(null) }
    var name by remember { mutableStateOf("") }
    var role by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var age by remember { mutableStateOf("") }
    var gender by remember { mutableStateOf("") }
    var appearance by remember { mutableStateOf("") }
    var personality by remember { mutableStateOf("") }
    var abilities by remember { mutableStateOf("") }
    var notes by remember { mutableStateOf("") }

    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(characterId) {
        if (characterId != -1) {
            val loadedCharacter = characterViewModel.getCharacterById(characterId)
            character = loadedCharacter
            loadedCharacter?.let {
                name = it.name
                role = it.role
                description = it.description
                age = it.age.toString()
                gender = it.gender
                appearance = it.appearance
                personality = it.personality
                abilities = it.abilities
                notes = it.notes
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
            text = if (characterId == -1) "Новий персонаж" else "Деталі персонажа",
            style = MaterialTheme.typography.headlineSmall,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        OutlinedTextField(value = name, onValueChange = { name = it }, label = { Text("Ім'я") }, modifier = Modifier.fillMaxWidth())
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(value = role, onValueChange = { role = it }, label = { Text("Роль") }, modifier = Modifier.fillMaxWidth())
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(value = description, onValueChange = { description = it }, label = { Text("Опис") }, modifier = Modifier.fillMaxWidth())
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(value = age, onValueChange = { age = it }, label = { Text("Вік") }, modifier = Modifier.fillMaxWidth())
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(value = gender, onValueChange = { gender = it }, label = { Text("Стать") }, modifier = Modifier.fillMaxWidth())
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(value = appearance, onValueChange = { appearance = it }, label = { Text("Зовнішність") }, modifier = Modifier.fillMaxWidth())
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(value = personality, onValueChange = { personality = it }, label = { Text("Характер") }, modifier = Modifier.fillMaxWidth())
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(value = abilities, onValueChange = { abilities = it }, label = { Text("Здібності") }, modifier = Modifier.fillMaxWidth())
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(value = notes, onValueChange = { notes = it }, label = { Text("Нотатки") }, modifier = Modifier.fillMaxWidth())
        Spacer(modifier = Modifier.height(16.dp))

        Row {
            Button(
                onClick = {
                    if (characterId == -1) {
                        characterViewModel.addCharacter(
                            name = name,
                            role = role,
                            description = description,
                            age = age.toIntOrNull() ?: 0,
                            gender = gender,
                            appearance = appearance,
                            personality = personality,
                            abilities = abilities,
                            notes = notes,
                            projectId = projectId
                        )
                    } else {
                        val updatedCharacter = character!!.copy(
                            name = name,
                            role = role,
                            description = description,
                            age = age.toIntOrNull() ?: 0,
                            gender = gender,
                            appearance = appearance,
                            personality = personality,
                            abilities = abilities,
                            notes = notes
                        )
                        characterViewModel.updateCharacter(updatedCharacter)
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

        Spacer(modifier = Modifier.height(16.dp))

        Spacer(modifier = Modifier.height(16.dp))
        Button(
            onClick = {
                coroutineScope.launch {
                    snackbarHostState.showSnackbar("📄 Персонаж експортовано у PDF та надіслано")
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Експортувати в PDF і надіслати")
        }


        Button(
            onClick = {
                characterViewModel.fetchCharactersFromCloud()
                coroutineScope.launch {
                    snackbarHostState.showSnackbar("✅ Синхронізовано з хмарою")
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Синхронізувати з хмарою")
        }
    }
}


