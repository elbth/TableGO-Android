package com.example.tablego.data.local

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.tablego.data.Event

@Entity(tableName = "events")
data class EventEntity(

    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val name: String,
    val date: String,
    val cost: Int,
    val description: String,

    @ColumnInfo(name = "created_at")
    val createdAt: Long = System.currentTimeMillis()
)
fun EventEntity.toDomain(): Event {
    return Event(
        id = id,
        name = name,
        date = date,
        cost = cost,
        description = description
    )
}

fun Event.toEntity(): EventEntity {
    return EventEntity(
        id = id,
        name = name,
        date = date,
        cost = cost,
        description = description
    )
}

