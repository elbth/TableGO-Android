package com.example.tablego.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.tablego.data.local.entity.ReviewEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ReviewDao {

    @Query("""
        SELECT * FROM reviews
        WHERE eventId = :eventId
        ORDER BY createdAt DESC
    """)
    fun getReviewsForEvent(eventId: Int): Flow<List<ReviewEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertReview(review: ReviewEntity)
}
