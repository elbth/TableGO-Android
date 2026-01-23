package com.example.tablego.ui.eventlist

import androidx.lifecycle.ViewModel
import com.example.tablego.data.Event
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class EventListViewModel : ViewModel() {

    private val _events = MutableStateFlow(
        listOf(
            Event(1, "Anime Expo", "Jan 12–13, 2025", 250),
            Event(2, "Comic Con", "Feb 2, 2025", 300)
        )
    )

    val events: StateFlow<List<Event>> = _events
}
