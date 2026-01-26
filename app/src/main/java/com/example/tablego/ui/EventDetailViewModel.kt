package com.example.tablego.ui

import androidx.lifecycle.ViewModel
import com.example.tablego.data.Event
import com.example.tablego.data.FakeEventRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class EventDetailViewModel() : ViewModel() {

    private val _event = MutableStateFlow<Event?>(null)
    val event: StateFlow<Event?> = _event

    fun loadEvent(eventId: Int) {
        _event.value = FakeEventRepository.getEventById(eventId)
    }
}
