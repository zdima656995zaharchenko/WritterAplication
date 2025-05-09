
package com.example.writteraplication.data.repository

import com.example.writteraplication.local.model.TimelineDao
import com.example.writteraplication.local.model.TimelineEntity
import kotlinx.coroutines.flow.Flow

class TimelineRepository(private val timelineDao: TimelineDao) {

    val allTimelines: Flow<List<TimelineEntity>> = timelineDao.getAllTimelines()

    fun getTimelineByProject(projectId: Int): Flow<List<TimelineEntity>> {
        return timelineDao.getTimelineByProject(projectId)
    }

    suspend fun getTimelineById(id: Int): TimelineEntity? = timelineDao.getTimelineById(id)

    suspend fun insertTimeline(timeline: TimelineEntity): Long = timelineDao.insertTimeline(timeline)

    suspend fun updateTimeline(timeline: TimelineEntity) = timelineDao.updateTimeline(timeline)

    suspend fun deleteTimeline(timeline: TimelineEntity) = timelineDao.deleteTimeline(timeline)
}
