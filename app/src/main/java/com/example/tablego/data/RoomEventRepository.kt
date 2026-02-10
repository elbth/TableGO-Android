package com.example.tablego.data

import com.example.tablego.data.local.EventDao
import com.example.tablego.data.local.toDomain
import com.example.tablego.data.local.toEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class RoomEventRepository(
    private val eventDao: EventDao
) : EventRepository {

    override fun getEvents(): Flow<List<Event>> {
        return eventDao
            .getAllEvents()
            .map { entities ->
                entities.map { it.toDomain() }
            }
    }

    override suspend fun getEventById(id: Int): Event? {
        return eventDao.getEventById(id)?.toDomain()
    }

    override suspend fun addEvent(event: Event) {
        eventDao.insertEvent(event.toEntity())
    }
}
