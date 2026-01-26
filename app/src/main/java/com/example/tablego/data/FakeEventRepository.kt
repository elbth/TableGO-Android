package com.example.tablego.data

// object = singleton
// one source of truth for events
//and every VM uses the same data
object FakeEventRepository {

    private val events = listOf(
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

    // returns all events to event list screen
    // enforces encapsulation
    fun getEvents(): List<Event> {
        return events
    }

    // returns one event to event details screen
    fun getEventById(id: Int): Event? {
        return events.find { it.id == id }
    }
}
