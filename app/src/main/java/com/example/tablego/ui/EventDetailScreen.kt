package com.example.tablego.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
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
    // this runs one the screen opens because we can't load data directly during composition
    // also, side effects must live in LaunchedEffect - load data ONCE when screen opens
    // basically, "when this screen appears, load the event"
    LaunchedEffect(eventId) {
        viewModel.loadEvent(eventId)
    }

    val event by viewModel.event.collectAsState()

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
        }
    }
}
