package com.example.writteraplication.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.writteraplication.local.model.CharacterEntity
import com.example.writteraplication.data.repository.CharacterRepository
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class CharacterViewModel(private val repository: CharacterRepository) : ViewModel() {

    private val _characters = MutableStateFlow<List<CharacterEntity>>(emptyList())
    val characters: StateFlow<List<CharacterEntity>> = _characters.asStateFlow()

    fun loadCharactersByProject(projectId: Int) {
        viewModelScope.launch {
            repository.getCharactersByProject(projectId)
                .catch { e -> Log.e("CharacterViewModel", "Помилка завантаження персонажів", e) }
                .collect { characters ->
                    _characters.value = characters
                }
        }
    }

    fun addCharacter(
        name: String,
        role: String,
        description: String,
        age: Int,
        gender: String,
        appearance: String,
        personality: String,
        abilities: String,
        notes: String,
        projectId: Int
    ) {
        viewModelScope.launch {
            Log.d("CharacterViewModel", "🟡 Додаємо персонажа: $name ($projectId)")
            val character = CharacterEntity(
                name = name,
                role = role,
                description = description,
                age = age,
                gender = gender,
                appearance = appearance,
                personality = personality,
                abilities = abilities,
                notes = notes,
                projectId = projectId
            )
            val result = repository.insertCharacter(character)
            Log.d("CharacterViewModel", "✅ Вставка завершена. ID нового персонажа: $result")

            // Оновити список після вставки
            loadCharactersByProject(projectId)
        }
    }

    fun updateCharacter(character: CharacterEntity) {
        viewModelScope.launch {
            repository.updateCharacter(character)
            loadCharactersByProject(character.projectId)
        }
    }

    fun removeCharacter(character: CharacterEntity) {
        viewModelScope.launch {
            repository.deleteCharacter(character)
            loadCharactersByProject(character.projectId)
        }
    }

    suspend fun getCharacterById(id: Int): CharacterEntity? {
        return repository.getCharacterById(id)
    }
}
