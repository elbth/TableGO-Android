package com.example.tablego.ui

import EventDetailViewModel
import EventDetailViewModelFactory
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.*
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.tablego.data.FakeEventRepository
import com.example.tablego.data.RoomReviewRepository
import com.example.tablego.data.local.AppDatabase

@Composable
fun EventDetailScreen(
    eventId: Int,
    onBack: () -> Unit
) {
    val context = LocalContext.current

    val viewModel: EventDetailViewModel = viewModel(
        factory = EventDetailViewModelFactory(
            eventRepository = FakeEventRepository(),
            reviewRepository = RoomReviewRepository(
                AppDatabase.getInstance(context).reviewDao()
            )
        )
    )

    val event by viewModel.event.collectAsState()
    val reviews by viewModel.reviews.collectAsState()
    var showReviewSheet by remember { mutableStateOf(false) }
    var selectedSort by remember { mutableStateOf(EventDetailViewModel.ReviewSort.NEWEST) }


    // this runs one the screen opens because we can't load data directly during composition
    // also, side effects must live in LaunchedEffect - load data ONCE when screen opens
    // basically, "when this screen appears, load the event"
    LaunchedEffect(eventId) {
        viewModel.loadEvent(eventId)
    }

    // loading/invalid ID case
    if (event == null) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
        }
        return
    }

    event?.let { e ->
        Box(modifier = Modifier.fillMaxSize()) {
            // Main content scrollable column
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                IconButton(onClick = onBack) {
                    Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                }

                Text(
                    text = e.name,
                    style = MaterialTheme.typography.headlineMedium
                )

                Text(text = "Date: ${e.date}")
                Text(text = "Table Cost: $${e.cost}")

                Divider()

                Text(
                    text = e.description,
                    style = MaterialTheme.typography.bodyLarge
                )

                Spacer(Modifier.height(16.dp))
                // event reviews
                Divider(modifier = Modifier.padding(vertical = 8.dp))

                Text(
                    text = "Reviews",
                    style = MaterialTheme.typography.titleMedium
                )

                // review sort buttons
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    Button(
                        onClick = {
                            selectedSort = EventDetailViewModel.ReviewSort.NEWEST
                            viewModel.sortReviews(selectedSort)
                        },
                        colors = if (selectedSort == EventDetailViewModel.ReviewSort.NEWEST) ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.primary
                        ) else ButtonDefaults.buttonColors()
                    ) {
                        Text("Newest")
                    }

                    Button(
                        onClick = {
                            selectedSort = EventDetailViewModel.ReviewSort.HIGHEST_RATED
                            viewModel.sortReviews(selectedSort)
                        },
                        colors = if (selectedSort == EventDetailViewModel.ReviewSort.HIGHEST_RATED) ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.primary
                        ) else ButtonDefaults.buttonColors()
                    ) {
                        Text("Highest Rated")
                    }
                }

//            Button(onClick = { showReviewSheet = true }) {
//                Text("Add Review")
//            }

                if (reviews.isEmpty()) {
                    Text(
                        text = "No reviews yet",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                } else {
                    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                        reviews.forEach { review ->
                            ReviewItem(review)
                        }
                    }
                }
            }
            // Floating button at bottom right
            FloatingActionButton(
                onClick = { showReviewSheet = true },
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary,
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(16.dp)
            ) {
                Icon(Icons.Default.Add, contentDescription = "Add Review")
            }
        }
    }

    if (showReviewSheet) {
        AddReviewBottomSheet(
            onDismiss = { showReviewSheet = false },
            onSubmit = { title, rating, body ->
                viewModel.addReview(
                    eventId = event!!.id,
                    title = title,
                    rating = rating,
                    body = body
                )
                showReviewSheet = false
            }
        )
    }

}
