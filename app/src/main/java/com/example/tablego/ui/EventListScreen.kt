package com.example.tablego.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.tablego.data.Event
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FilterList

// PREVIEW DATA
// dummy events for preview only
private val fakeEvents = listOf(
    Event(id = 1, name = "Art Market", date = "Jan. 12-13, 2025", cost = 50, description = "Large" +
            " anime convention with high foot traffic."
    ),
    Event(id = 2, name = "Comic Con", date = "Feb. 3-5, 2025", cost = 100, description = "Small " +
            "local market with handmade goods."
    ),
    Event(id = 3, name = "Music Festival", date = "Mar. 15-16, 2025", cost = 75, description = "Multi-day comic expo with national vendors."
    )
)

// PREVIEWS
@Preview(showBackground = true)
@Composable
fun EventListPreview() {
    MaterialTheme {
        EventListContent(
            events = fakeEvents,
            onEventClick = {}
        )
    }
}
// REUSABLE CONTENT
// IDE-only preview
@Composable
fun EventListContent(
    events: List<Event>,
    onEventClick: (Int) -> Unit
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(events) { event ->
            EventCard(
                event = event,
                onClick = { onEventClick(event.id) }
            )
        }
    }
}


// REAL SCREEN
// indicates a function converts data to UI for Jetpack Compose
// enables Compose compiler to process it
@Composable
fun EventListScreen(
    viewModel: EventListViewModel = viewModel(),
    onEventClick: (Int) -> Unit
) {
    val events by viewModel.events.collectAsState()
    // state for filters
    var selectedMonth by remember { mutableStateOf("") }
    var showFilterCard by remember { mutableStateOf(false) }
    var maxCost by remember { mutableStateOf("") }
    var query by remember { mutableStateOf("") }
    var selectedYear by remember { mutableStateOf(0) }

    Column(modifier = Modifier
        .fillMaxSize()
        .padding(8.dp)
    ) {

        // Top row: Search + Filter + Reset
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Search Bar
            OutlinedTextField(
                value = query,
                onValueChange = { value ->
                    query = value
                    viewModel.applyFilters(
                        searchQuery = query,
                        month = selectedMonth,
                        maxCost = maxCost.toIntOrNull() ?: Int.MAX_VALUE,
                        year = selectedYear
                    )
                },
                label = { Text("Search events") },
                modifier = Modifier.weight(1f)
            )

            // Filter Icon
            IconButton(onClick = { showFilterCard = !showFilterCard }) {
                Icon(Icons.Filled.FilterList, contentDescription = "Filter")
            }

            // Reset button
            Button(
                onClick = {
                    query = ""
                    selectedMonth = ""
                    maxCost = ""
                    selectedYear = 0
                    viewModel.resetFilters() // NEW
                }
            ) {
                Text("Reset")
            }
        }

        Spacer(Modifier.height(8.dp))

        // FILTER CARD (Expandable)
        if (showFilterCard) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                elevation = CardDefaults.cardElevation(4.dp)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {

                    // Month dropdown
                    MonthDropdown(
                        selectedMonth = selectedMonth,
                        onMonthSelected = { month ->
                            selectedMonth = month
                        }
                    )

                    // Max cost input
                    OutlinedTextField(
                        value = maxCost,
                        onValueChange = { value ->
                            maxCost = value.filter { it.isDigit() }
                        },
                        label = { Text("Max Cost") },
                        modifier = Modifier.fillMaxWidth(),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number) // NEW
                    )

                    // Apply filters button
                    Button(
                        onClick = {
                            val costInt = maxCost.toIntOrNull() ?: Int.MAX_VALUE
                            viewModel.applyFilters(
                                searchQuery = query,
                                month = selectedMonth,
                                maxCost = costInt,
                                year = selectedYear
                            )
                            showFilterCard = false
                        },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Apply Filters")
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // --- Year Filter Row ---
        val years = viewModel.getYears()
        if (years.isNotEmpty()) {
            Row(
                modifier = Modifier.horizontalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                years.forEach { year ->
                    Button(
                        onClick = {
                            selectedYear = year
                            viewModel.applyFilters(
                                searchQuery = query,
                                month = selectedMonth,
                                maxCost = maxCost.toIntOrNull() ?: Int.MAX_VALUE,
                                year = selectedYear
                            )
                        }
                    ) {
                        Text(year.toString())
                    }
                }
            }
            Spacer(Modifier.height(8.dp))
        }

        // EVENT LIST
        if (events.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .weight(1f),
                contentAlignment = Alignment.Center
            ) {
                Text("No events found")
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .weight(1f),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(events) { event ->
                    EventCard(event) { onEventClick(event.id) }
                }
            }
        }
    }
}



// EVENT CARD
@Composable
fun EventCard(
    event: Event,
    onClick: () -> Unit
) {
    // card layout, using MaterialTheme.typography AKA Material 3 standards
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            // event name = title
            Text(
                text = event.name,
                style = MaterialTheme.typography.titleMedium
            )

            Spacer(modifier = Modifier.height(4.dp)) // add small gap between text
            
            // event date = subtitle
            Text(
                text = event.date,
                style = MaterialTheme.typography.bodyMedium,
                // make text lighter, less dominant
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(8.dp))

            // cost = badge-like or pill
            Box(
                modifier = Modifier
                    .wrapContentSize()
                    .background(
                        color = MaterialTheme.colorScheme.primary.copy(alpha = 0.1f),
                        shape = RoundedCornerShape(8.dp)
                    )
                    .padding(horizontal = 8.dp, vertical = 4.dp)
            ) {
                Text(
                    text = "$${event.cost}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }
    }
}

// Month dropdown component
@Composable
fun MonthDropdown(
    selectedMonth: String,
    onMonthSelected: (String) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    val months = listOf(
        "Jan", "Feb", "Mar", "Apr", "May", "Jun",
        "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"
    )

    Box(modifier = Modifier
        .fillMaxWidth()
        .padding(horizontal = 16.dp, vertical = 8.dp)
    ) {
        Button(
            onClick = { expanded = true },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = if (selectedMonth.isEmpty()) "Select Month" else selectedMonth)
        }

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            months.forEach { month ->
                DropdownMenuItem(
                    text = { Text(month) },
                    onClick = {
                        onMonthSelected(month)
                        expanded = false
                    }
                )
            }
        }
    }
}