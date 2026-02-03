package com.example.tablego.data

data class Review(
    val id: Int,
    val eventId: Int,
    val title: String,
    val rating: Int,
    val body: String,
    val createdAt: Long = System.currentTimeMillis()
)
