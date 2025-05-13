
package com.example.writteraplication.viewmodel

import android.content.Context
import java.util.UUID
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.writteraplication.local.model.TimelineEntity
import com.example.writteraplication.local.model.CharacterEntity
import com.example.writteraplication.data.repository.TimelineRepository
import com.example.writteraplication.data.repository.FirebaseTimelineRepository
import com.example.writteraplication.utils.createPdfFromPlots
import com.example.writteraplication.utils.createPdfFromTimelines
import com.example.writteraplication.utils.sendEmailWithAttachment
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class TimelineViewModel(
    private val repository: TimelineRepository,
    private val firebaseTimelineRepository: FirebaseTimelineRepository
) : ViewModel() {

    private val _timeline = MutableStateFlow<List<TimelineEntity>>(emptyList())
    val timeline: StateFlow<List<TimelineEntity>> = _timeline.asStateFlow()

    private val _eventElements = MutableStateFlow<List<EventElement>>(emptyList())
    val eventElements: StateFlow<List<EventElement>> = _eventElements

    private val _characters = MutableStateFlow<List<CharacterEntity>>(emptyList())
    val characters: StateFlow<List<CharacterEntity>> = _characters

    fun loadTimelineByProject(projectId: Int) {
        viewModelScope.launch {
            repository.getTimelineByProject(projectId)
                .catch { e -> Log.e("TimelineViewModel", "Помилка завантаження подій", e) }
                .collect { timeline ->
                    _timeline.value = timeline
                }
        }
    }

    fun loadCharactersByProject(projectId: Int) {
        viewModelScope.launch {
            repository.getCharactersByProject(projectId)
                .catch { e -> Log.e("TimelineViewModel", "Помилка завантаження персонажів", e) }
                .collect { characters ->
                    _characters.value = characters
                }
        }
    }

    fun addTimeline(
        title: String,
        description: String,
        eventDate: String?,
        projectId: Int,
        characters: List<String>
    ) {
        viewModelScope.launch {
            Log.d("TimelineViewModel", "🟡 Додаємо подію: $title ($projectId)")
            val timeline = TimelineEntity(
                title = title,
                description = description,
                eventDate = eventDate,
                projectId = projectId,
                characters = characters
            )
            val result = repository.insertTimeline(timeline)
            Log.d("TimelineViewModel", "✅ Вставка завершена. ID нової події: $result")

            firebaseTimelineRepository.saveTimeline(timeline)

            // Оновити список після вставки
            loadTimelineByProject(projectId)
        }
    }

    fun updateTimeline(timeline: TimelineEntity) {
        viewModelScope.launch {
            repository.updateTimeline(timeline)
            firebaseTimelineRepository.saveTimeline(timeline)
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

    // Методи для роботи з елементами події
    fun addEventElement() {
        val newElement = EventElement(UUID.randomUUID().toString(), "Подія")
        _eventElements.value += newElement
    }

    fun updateEventElement(id: String, title: String, date: String, characters: List<String>) {
        _eventElements.value = _eventElements.value.map {
            if (it.id == id) it.copy(title = title, date = date, characters = characters) else it
        }
    }

    fun removeEventElement(id: String) {
        _eventElements.value = _eventElements.value.filter { it.id != id }
    }

    fun fetchTimelinesFromCloud() {
        viewModelScope.launch {
            val cloudTimelines = firebaseTimelineRepository.getTimelines()
            _timeline.value = cloudTimelines
        }
    }

    fun exportTimelineToPdfAndSend(context: Context) {
        viewModelScope.launch {
            try {
                val cloudTimelines = firebaseTimelineRepository.getTimelines()
                if (cloudTimelines.isNotEmpty()) {
                    val pdfFile = createPdfFromTimelines(context, cloudTimelines, "TimelinesExport")
                    val email = FirebaseAuth.getInstance().currentUser?.email
                    if (email != null) {
                        sendEmailWithAttachment(context, pdfFile, email)
                    } else {
                        Log.w("TimelineViewModel", "❗ Email користувача не знайдено")
                    }
                } else {
                    Log.w("TimelineViewModel", "❗ Немає сюжетів для експорту")
                }
            } catch (e: Exception) {
                Log.e("TimelineViewModel", "❌ Помилка експорту сюжетів", e)
            }
        }
    }
}

