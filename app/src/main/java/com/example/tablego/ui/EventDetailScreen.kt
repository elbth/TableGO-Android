package com.example.tablego.ui

import EventDetailViewModel
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.*
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun EventDetailScreen(
    eventId: Int,
    viewModel: EventDetailViewModel = viewModel()
) {

    val event by viewModel.event.collectAsState()
    val reviews by viewModel.reviews.collectAsState()
    var showReviewSheet by remember { mutableStateOf(false) }

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
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
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
            Divider(modifier = Modifier.padding(vertical = 16.dp))

            Text(
                text = "Reviews",
                style = MaterialTheme.typography.titleMedium
            )
            Spacer(Modifier.height(8.dp))

            // NEW
            if (reviews.isEmpty()) {
                Text(
                    text = "No reviews yet",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            } else {
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(reviews) { review ->
                        ReviewItem(review)
                    }
                }
            }


            Spacer(modifier = Modifier.height(12.dp))

            Button(onClick = { showReviewSheet = true }) {
                Text("Add Review")
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
