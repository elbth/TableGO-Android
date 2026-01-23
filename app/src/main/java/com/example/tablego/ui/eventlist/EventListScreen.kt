package com.example.tablego.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.tablego.data.Event
import com.example.tablego.ui.eventlist.EventListViewModel

@Composable
fun EventListScreen(
    viewModel: EventListViewModel = viewModel()
) {
    // collectAsState() converts events to Compose State and triggers recomposition
    // when VM updates list, UI updates automatically
    val events by viewModel.events.collectAsState()

    // similar to recyclerView, only renders visible items, allows scroll, and recomposes when
    // needed
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // for every event, draw a card
        items(events) { event ->
            EventCard(event)
        }
    }
}

@Composable
fun EventCard(event: Event) {
    // card layout
    Card(
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = event.name)
            Text(text = event.date)
            Text(
                text = "Table Cost: $${event.cost}",
                style = MaterialTheme.typography.bodySmall
            )
        }
    }
}
