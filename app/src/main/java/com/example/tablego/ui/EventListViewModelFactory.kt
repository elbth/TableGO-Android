import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.tablego.data.EventRepository
import com.example.tablego.ui.EventListViewModel

class EventListViewModelFactory(
    private val eventRepository: EventRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(EventListViewModel::class.java)) {
            return EventListViewModel(eventRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
