package com.example.tablego.data

import androidx.room.ColumnInfo

data class Review(
    val id: Int,
    val eventId: Int,
    val title: String,
    val rating: Int,
    val body: String,
    @ColumnInfo(name = "created_at")
    val createdAt: Long = System.currentTimeMillis()
)
