
package com.example.writteraplication.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.writteraplication.local.model.TimelineEntity
import com.example.writteraplication.data.repository.TimelineRepository
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class TimelineViewModel(private val repository: TimelineRepository) : ViewModel() {

    private val _timeline = MutableStateFlow<List<TimelineEntity>>(emptyList())
    val timeline: StateFlow<List<TimelineEntity>> = _timeline.asStateFlow()

    fun loadTimelineByProject(projectId: Int) {
        viewModelScope.launch {
            repository.getTimelineByProject(projectId)
                .catch { e -> Log.e("TimelineViewModel", "Помилка завантаження подій", e) }
                .collect { timeline ->
                    _timeline.value = timeline
                }
        }
    }

    fun addTimeline(
        title: String,
        description: String,
        eventDate: String?,
        projectId: Int
    ) {
        viewModelScope.launch {
            Log.d("TimelineViewModel", "🟡 Додаємо подію: $title ($projectId)")
            val timeline = TimelineEntity(
                title = title,
                description = description,
                eventDate = eventDate,
                projectId = projectId
            )
            val result = repository.insertTimeline(timeline)
            Log.d("TimelineViewModel", "✅ Вставка завершена. ID нової події: $result")

            // Оновити список після вставки
            loadTimelineByProject(projectId)
        }
    }

    fun updateTimeline(timeline: TimelineEntity) {
        viewModelScope.launch {
            repository.updateTimeline(timeline)
            loadTimelineByProject(timeline.projectId)
        }
    }

    fun removeTimeline(timeline: TimelineEntity) {
        viewModelScope.launch {
            repository.deleteTimeline(timeline)
            loadTimelineByProject(timeline.projectId)
        }
    }

    suspend fun getTimelineById(id: Int): TimelineEntity? {
        return repository.getTimelineById(id)
    }
}
