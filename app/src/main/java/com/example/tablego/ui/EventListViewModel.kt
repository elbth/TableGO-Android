package com.example.tablego.ui

import androidx.lifecycle.ViewModel
import com.example.tablego.data.Event
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update

class EventListViewModel : ViewModel() {

    // All events (unfiltered)
    private val allEvents = listOf(
        Event(1,
            "Anime Expo",
            "Jan 12–13, 2025", 250,
            "The largest anime convention in North America"),
        Event(2,
            "Comic Con",
            "Feb 2, 2025", 300,
            "A huge pop culture convention with comics, movies, and games"),
        Event(3,
            "Music Festival",
            "Mar. 15-16, 2025", 75,
            "Multi-day comic expo with national vendors."),
        Event(4,
            "Art Market",
            "Jan. 20-21, 2025", 50,
            "Local handmade art market")
    )

    // Events currently displayed in UI
    private val _events = MutableStateFlow(allEvents)
    val events: StateFlow<List<Event>> = _events

    // Search events by keyword in name or description
    fun search(query: String) {
        applyFilters(searchQuery = query, month = "", maxCost = Int.MAX_VALUE)
    }

    // apply combined filters
    fun applyFilters(searchQuery: String, month: String, maxCost: Int) {
        val filtered = allEvents.filter { event ->
            val matchesQuery = searchQuery.isBlank() ||
                    event.name.contains(searchQuery, ignoreCase = true) ||
                    event.description.contains(searchQuery, ignoreCase = true)

            val matchesMonth = month.isBlank() || event.date.contains(month, ignoreCase = true)

            val matchesCost = event.cost <= maxCost

            matchesQuery && matchesMonth && matchesCost
        }

        _events.value = filtered
    }

    // reset filters for user
    fun resetFilters() {
        _events.value = allEvents
    }
}
