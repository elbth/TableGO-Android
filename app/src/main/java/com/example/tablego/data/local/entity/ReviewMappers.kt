package com.example.tablego.data.local.entity

import com.example.tablego.data.Review

fun ReviewEntity.toDomain(): Review {
    return Review(
        id = id,
        eventId = eventId,
        title = title,
        rating = rating,
        body = body,
        createdAt = createdAt
    )
}

fun Review.toEntity(): ReviewEntity {
    return ReviewEntity(
        id = id,
        eventId = eventId,
        title = title,
        rating = rating,
        body = body,
        createdAt = createdAt
    )
}
