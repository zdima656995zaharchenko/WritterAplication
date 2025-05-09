
package com.example.writteraplication.local.model

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface TimelineDao {

    @Query("SELECT * FROM timeline WHERE project_id = :projectId")
    fun getTimelineByProject(projectId: Int): Flow<List<TimelineEntity>>

    @Query("SELECT * FROM timeline WHERE id = :id")
    suspend fun getTimelineById(id: Int): TimelineEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTimeline(timeline: TimelineEntity): Long

    @Update
    suspend fun updateTimeline(timeline: TimelineEntity)

    @Delete
    suspend fun deleteTimeline(timeline: TimelineEntity)

    @Query("SELECT * FROM timeline")
    fun getAllTimelines(): Flow<List<TimelineEntity>>
}
