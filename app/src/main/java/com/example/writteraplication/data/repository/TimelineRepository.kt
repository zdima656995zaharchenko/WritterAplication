
package com.example.writteraplication.data.repository

import com.example.writteraplication.local.model.TimelineDao
import com.example.writteraplication.local.model.TimelineEntity
import com.example.writteraplication.local.model.CharacterDao
import com.example.writteraplication.local.model.CharacterEntity
import kotlinx.coroutines.flow.Flow

class TimelineRepository(
    private val timelineDao: TimelineDao,
    private val characterDao: CharacterDao
) {
    val allTimelines: Flow<List<TimelineEntity>> = timelineDao.getAllTimelines()

    fun getTimelineByProject(projectId: Int): Flow<List<TimelineEntity>> {
        return timelineDao.getTimelineByProject(projectId)
    }

    fun getCharactersByProject(projectId: Int): Flow<List<CharacterEntity>> {
        return characterDao.getCharactersByProject(projectId)
    }

    suspend fun getTimelineById(id: Int): TimelineEntity? = timelineDao.getTimelineById(id)
    suspend fun insertTimeline(timeline: TimelineEntity): Long = timelineDao.insertTimeline(timeline)
    suspend fun updateTimeline(timeline: TimelineEntity) = timelineDao.updateTimeline(timeline)
    suspend fun deleteTimeline(timeline: TimelineEntity) = timelineDao.deleteTimeline(timeline)
}

