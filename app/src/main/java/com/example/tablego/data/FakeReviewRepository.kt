import com.example.tablego.data.Review
import com.example.tablego.data.ReviewRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map

class FakeReviewRepository : ReviewRepository {

    private val reviews = mutableListOf<Review>()
    private val reviewsFlow = MutableStateFlow<List<Review>>(emptyList())

    override fun getReviewsForEvent(eventId: Int): Flow<List<Review>> {
        return reviewsFlow.map { list ->
            list.filter { it.eventId == eventId }
        }
    }

    override suspend fun addReview(review: Review) {
        reviews.add(review)
        reviewsFlow.value = reviews.toList()
    }
}
