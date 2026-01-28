package com.example.tablego.data

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.Flow

class FakeReviewRepository : ReviewRepository {

    private val reviewsFlow = MutableStateFlow<List<Review>>(
        listOf(
            Review(
                id = 1,
                eventId = 1,
                title = "Great event!",
                rating = 5,
                body = "Tons of people and great sales."
            ),
            Review(
                id = 2,
                eventId = 1,
                title = "Very crowded",
                rating = 3,
                body = "Good exposure but long days."
            )
        )
    )

    override fun getReviewsForEvent(eventId: Int): Flow<List<Review>> {
        return MutableStateFlow(
            reviewsFlow.value.filter { it.eventId == eventId }
        )
    }

    override fun addReview(review: Review) {
        reviewsFlow.value = reviewsFlow.value + review
    }
}
