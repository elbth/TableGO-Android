package com.example.tablego.data

import kotlinx.coroutines.flow.Flow

// define what data the app needs
interface EventRepository {
    fun getEvents(): Flow<List<Event>>
    suspend fun getEventById(id: Int): Event?
    suspend fun addEvent(event: Event)

}
