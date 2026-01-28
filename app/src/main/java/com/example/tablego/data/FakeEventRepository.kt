package com.example.tablego.data

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow

// object = singleton
// one source of truth for events
//and every VM uses the same data
class FakeEventRepository : EventRepository {

    private val sampleEvents = listOf(
        Event(
            id = 1,
            name = "Anime Convention",
            date = "Jan 12–13, 2025",
            cost = 250,
            description = "Large anime convention with high foot traffic."
        ),
        Event(
            id = 2,
            name = "Local Art Market",
            date = "Feb 3, 2025",
            cost = 75,
            description = "Small local market with handmade goods."
        ),
        Event(
            id = 3,
            name = "Comic Expo",
            date = "Mar 20–22, 2025",
            cost = 300,
            description = "Multi-day comic expo with national vendors."
        )
    )

    private val eventsFlow = MutableStateFlow(sampleEvents)


    // returns all events to event list screen
    // enforces encapsulation
    override fun getEvents(): Flow<List<Event>> = eventsFlow


    // returns one event to event details screen
    override fun getEventById(id: Int): Event? {
        return eventsFlow.value.find { it.id == id }
    }

    override fun searchEvents(
        query: String,
        month: String,
        maxCost: Int,
        year: Int
    ): Flow<List<Event>> {
        val filtered = eventsFlow.value.filter { event ->
            (query.isBlank() || event.name.contains(query, true) || event.description.contains(query, true)) &&
                    (month.isBlank() || event.date.contains(month)) &&
                    (event.cost <= maxCost) &&
                    (event.date.contains(year.toString()))
        }
        return MutableStateFlow(filtered)
    }
}
