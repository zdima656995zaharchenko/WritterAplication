package com.example.writteraplication.local.model

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface PlotDao {

    @Query("SELECT * FROM plots")
    fun getAllPlots(): Flow<List<PlotEntity>>

    @Query("SELECT * FROM plots WHERE project_id = :projectId")
    fun getPlotsByProject(projectId: Int): Flow<List<PlotEntity>>

    @Insert
    suspend fun insertPlot(plot: PlotEntity): Long

    @Delete
    suspend fun deletePlot(plot: PlotEntity)

    @Query("SELECT * FROM plots WHERE id = :plotId")
    suspend fun getPlotById(plotId: Int): PlotEntity?

    @Update
    suspend fun updatePlot(plot: PlotEntity)
}
