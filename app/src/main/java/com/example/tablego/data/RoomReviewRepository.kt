package com.example.tablego.data

import com.example.tablego.data.local.dao.ReviewDao
import com.example.tablego.data.local.entity.toDomain
import com.example.tablego.data.local.entity.toEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class RoomReviewRepository(
    private val dao: ReviewDao
) : ReviewRepository {

    override fun getReviewsForEvent(eventId: Int): Flow<List<Review>> {
        return dao.getReviewsForEvent(eventId)
            .map { entities ->
                entities.map { it.toDomain() }
            }
    }

    override suspend fun addReview(review: Review) {
        dao.insertReview(review.toEntity())
    }
}
