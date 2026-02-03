import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tablego.data.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class EventDetailViewModel(
    private val eventRepository: EventRepository = FakeEventRepository(),
    private val reviewRepository: ReviewRepository = FakeReviewRepository()
) : ViewModel() {

    private val _event = MutableStateFlow<Event?>(null)
    val event: StateFlow<Event?> = _event

    private val _reviews = MutableStateFlow<List<Review>>(emptyList())
    val reviews: StateFlow<List<Review>> = _reviews

    fun loadEvent(eventId: Int) {
        _event.value = eventRepository.getEventById(eventId)
        loadReviews(eventId)
    }

    private fun loadReviews(eventId: Int) {
        viewModelScope.launch {
            reviewRepository.getReviewsForEvent(eventId).collectLatest {
                _reviews.value = it
            }
        }
    }

    fun addReview(
        eventId: Int,
        title: String,
        rating: Int,
        body: String
    ) {
        val newReview = Review(
            id = _reviews.value.size + 1,
            eventId = eventId,
            title = title,
            rating = rating,
            body = body
        )
        reviewRepository.addReview(newReview)
        // manually trigger a reload
        loadReviews(eventId)

    }
}
