import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.tablego.data.EventRepository
import com.example.tablego.data.ReviewRepository

class EventDetailViewModelFactory(
    private val eventRepository: EventRepository,
    private val reviewRepository: ReviewRepository
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(EventDetailViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return EventDetailViewModel(
                eventRepository,
                reviewRepository
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
