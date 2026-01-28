package com.example.tablego.data

import kotlinx.coroutines.flow.Flow

// define what data the app needs
interface EventRepository {
    fun getEvents(): Flow<List<Event>>
    fun getEventById(id: Int): Event?
    fun searchEvents(
        query: String,
        month: String,
        maxCost: Int,
        year: Int
    ): Flow<List<Event>>
}
