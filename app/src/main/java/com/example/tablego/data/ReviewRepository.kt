package com.example.tablego.data

import kotlinx.coroutines.flow.Flow

interface ReviewRepository {
    fun getReviewsForEvent(eventId: Int): Flow<List<Review>>
    suspend fun addReview(review: Review)
}
