package com.example.tablego.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "reviews")
data class ReviewEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val eventId: Int,
    val title: String,
    val rating: Int,
    val body: String,
    val createdAt: Long
)
