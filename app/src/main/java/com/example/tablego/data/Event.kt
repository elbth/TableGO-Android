package com.example.tablego.data

import androidx.room.Entity

@Entity(tableName = "events")

data class Event(
    val id: Int,
    val name: String,
    val date: String,
    val cost: Int,
    val description: String
)