package com.example.tablego.ui

import androidx.lifecycle.ViewModel
import com.example.tablego.data.Event
import com.example.tablego.data.EventRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class EventListViewModel(
    private val eventRepository: EventRepository
) : ViewModel() {

    // Current list shown in UI
    private val _events = MutableStateFlow<List<Event>>(emptyList())
    val events: StateFlow<List<Event>> = _events

    // Keep all events for filtering
    private var allEvents: List<Event> = emptyList()

    init {
        loadEvents()
    }

    private fun loadEvents() {
        viewModelScope.launch {
            eventRepository.getEvents().collect { list ->
                allEvents = list
                _events.value = list
            }
        }
    }

    // Apply combined filters
    fun applyFilters(
        searchQuery: String = "",
        month: String = "",
        maxCost: Int = Int.MAX_VALUE,
        year: Int = 0
    ) {
        _events.value = allEvents.filter { event ->
            val matchesQuery = searchQuery.isBlank() ||
                    event.name.contains(searchQuery, ignoreCase = true) ||
                    event.description.contains(searchQuery, ignoreCase = true)

            val matchesMonth = month.isBlank() || event.date.contains(month, ignoreCase = true)
            val matchesCost = event.cost <= maxCost
            val matchesYear = year == 0 || event.date.contains(year.toString())

            matchesQuery && matchesMonth && matchesCost && matchesYear
        }
    }

    fun resetFilters() {
        _events.value = allEvents
    }

    // Helper: list of years currently in the database
    fun getYears(): List<Int> {
        return allEvents.mapNotNull { event ->
            event.date.takeLast(4).toIntOrNull()
        }.distinct().sorted()
    }

    // add event to event list and database
    fun addEvent(
        name: String,
        date: String,
        cost: Int,
        description: String
    ) {
        viewModelScope.launch {

            val newEvent = Event(
                id = events.value.size + 1,
                name = name,
                date = date,
                cost = cost,
                description = description
            )

            eventRepository.addEvent(newEvent)
        }
    }

}
