import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tablego.data.*
import com.example.tablego.data.local.AppDatabase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class EventDetailViewModel(
    private val eventRepository: EventRepository,
    private val reviewRepository: ReviewRepository
) : ViewModel() {

    private val _event = MutableStateFlow<Event?>(null)
    val event: StateFlow<Event?> = _event

    private val _reviews = MutableStateFlow<List<Review>>(emptyList())
    val reviews: StateFlow<List<Review>> = _reviews

    // track current sort
    private var currentSort: ReviewSort = ReviewSort.NEWEST

    fun loadEvent(eventId: Int) {
        _event.value = eventRepository.getEventById(eventId)
        loadReviews(eventId)
    }

    private fun loadReviews(eventId: Int) {
        viewModelScope.launch {
            reviewRepository
                .getReviewsForEvent(eventId)
                .collectLatest { list ->
                    _reviews.value = applySort(list, currentSort)
            }
        }
    }

    fun addReview(
        eventId: Int,
        title: String,
        rating: Int,
        body: String) {
        viewModelScope.launch {
            val newReview = Review(
                id = _reviews.value.size + 1,
                eventId = eventId,
                title = title,
                rating = rating,
                body = body
            )
            reviewRepository.addReview(newReview)
            loadReviews(eventId)
        }
    }

    enum class ReviewSort { NEWEST, HIGHEST_RATED }

    fun sortReviews(sort: ReviewSort) {
        currentSort = sort
        _reviews.value = applySort(_reviews.value, sort)
    }

    private fun applySort(list: List<Review>, sort: ReviewSort): List<Review> {
        return when (sort) {
            ReviewSort.NEWEST -> list.sortedByDescending { it.createdAt }
            ReviewSort.HIGHEST_RATED -> list.sortedByDescending { it.rating }
        }
    }
}
