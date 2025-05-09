package com.example.writteraplication.data.repository

import com.example.writteraplication.local.model.PlotDao
import com.example.writteraplication.local.model.PlotEntity
import kotlinx.coroutines.flow.Flow

class PlotRepository(private val plotDao: PlotDao) {

    val allPlots: Flow<List<PlotEntity>> = plotDao.getAllPlots()

    fun getPlotsByProject(projectId: Int): Flow<List<PlotEntity>> {
        return plotDao.getPlotsByProject(projectId)
    }

    suspend fun insert(plot: PlotEntity): Long {
        return plotDao.insertPlot(plot)
    }

    suspend fun delete(plot: PlotEntity) {
        plotDao.deletePlot(plot)
    }

    suspend fun updatePlot(plot: PlotEntity) {
        plotDao.updatePlot(plot)
    }

    suspend fun getPlotById(id: Int): PlotEntity? {
        return plotDao.getPlotById(id)
    }
}
