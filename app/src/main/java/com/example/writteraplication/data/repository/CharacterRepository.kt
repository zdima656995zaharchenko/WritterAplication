package com.example.writteraplication.data.repository

import com.example.writteraplication.local.model.CharacterEntity
import com.example.writteraplication.local.model.CharacterDao
import kotlinx.coroutines.flow.Flow

class CharacterRepository(private val characterDao: CharacterDao) {

    val allCharacters: Flow<List<CharacterEntity>> = characterDao.getAllCharacters()

    fun getCharactersByProject(projectId: Int): Flow<List<CharacterEntity>> {
        return characterDao.getCharactersByProject(projectId)
    }

    suspend fun getCharacterById(id: Int): CharacterEntity? = characterDao.getCharacterById(id)

    suspend fun insertCharacter(character: CharacterEntity) = characterDao.insertCharacter(character)

    suspend fun updateCharacter(character: CharacterEntity) = characterDao.updateCharacter(character)

    suspend fun deleteCharacter(character: CharacterEntity) = characterDao.deleteCharacter(character)
}
