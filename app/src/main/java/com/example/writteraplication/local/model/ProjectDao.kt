package com.example.writteraplication.local.model

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Delete
import com.example.writteraplication.local.model.ProjectEntity

@Dao
interface ProjectDao {
    @Insert
    suspend fun insertProject(project: ProjectEntity): Long

    @Query("SELECT * FROM projects")
    suspend fun getAllProjects(): List<ProjectEntity>

    @Query("SELECT * FROM projects WHERE id = :id")
    suspend fun getProjectById(id: Int): ProjectEntity?

    @Delete
    suspend fun deleteProject(project: ProjectEntity)
}


