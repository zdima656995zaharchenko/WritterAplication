package com.example.writteraplication.data.repository

import com.example.writteraplication.local.model.ProjectDao
import com.example.writteraplication.local.model.ProjectEntity

class ProjectRepository(private val projectDao: ProjectDao) {
    suspend fun insertProject(project: ProjectEntity): Long {
        return projectDao.insertProject(project)
    }

    suspend fun getAllProjects(): List<ProjectEntity> {
        return projectDao.getAllProjects()
    }
    suspend fun getFavoriteProjects(): List<ProjectEntity> {
        return projectDao.getFavoriteProjects()
    }

    suspend fun getProjectById(id: Int): ProjectEntity? {
        return projectDao.getProjectById(id)
    }
    suspend fun updateProject(project: ProjectEntity) {
        projectDao.updateProject(project)
    }

    suspend fun deleteProject(project: ProjectEntity) {
        projectDao.deleteProject(project)
    }
}
