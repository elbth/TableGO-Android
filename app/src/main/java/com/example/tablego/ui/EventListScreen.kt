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
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material3.FloatingActionButton
import com.example.tablego.ui.AddEventBottomSheet

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
    var showAddEventSheet by remember { mutableStateOf(false) }
    var selectedMonth by remember { mutableStateOf("") }
    var showFilterCard by remember { mutableStateOf(false) }
    var maxCost by remember { mutableStateOf("") }
    var query by remember { mutableStateOf("") }
    var selectedYear by remember { mutableStateOf(0) }

    Box(modifier = Modifier.fillMaxSize()) {

        // Single LazyColumn for events
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            contentPadding = PaddingValues(bottom = 80.dp) // space for FAB
        ) {
            // Top search/filter section as first item
            item {
                Column(modifier = Modifier.fillMaxWidth()) {
                    // Search bar + filter + reset
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
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

                        IconButton(onClick = { showFilterCard = !showFilterCard }) {
                            Icon(Icons.Filled.FilterList, contentDescription = "Filter")
                        }

                        Button(
                            onClick = {
                                query = ""
                                selectedMonth = ""
                                maxCost = ""
                                selectedYear = 0
                                viewModel.resetFilters()
                            }
                        ) {
                            Text("Reset")
                        }
                    }

                    Spacer(Modifier.height(8.dp))

                    // Filter Card
                    if (showFilterCard) {
                        FilterCard(
                            selectedMonth = selectedMonth,
                            onMonthSelected = { selectedMonth = it },
                            maxCost = maxCost,
                            onMaxCostChange = { maxCost = it },
                            applyFilters = {
                                viewModel.applyFilters(
                                    searchQuery = query,
                                    month = selectedMonth,
                                    maxCost = maxCost.toIntOrNull() ?: Int.MAX_VALUE,
                                    year = selectedYear
                                )
                                showFilterCard = false
                            }
                        )
                    }


                    // Year buttons
                    val years = viewModel.getYears()
                    if (years.isNotEmpty()) {
                        Row(
                            modifier = Modifier.horizontalScroll(rememberScrollState()),
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            years.forEach { year ->
                                Button(onClick = {
                                    selectedYear = year
                                    viewModel.applyFilters(
                                        searchQuery = query,
                                        month = selectedMonth,
                                        maxCost = maxCost.toIntOrNull() ?: Int.MAX_VALUE,
                                        year = selectedYear
                                    )
                                }) {
                                    Text(year.toString())
                                }
                            }
                        }
                        Spacer(Modifier.height(8.dp))
                    }
                }
            }

            // Event items
            items(events) { event ->
                EventCard(event = event) { onEventClick(event.id) }
            }

            // If no events
            if (events.isEmpty()) {
                item {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 32.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("No events found")
                    }
                }
            }
        }

        // Floating Add Event Button
        FloatingActionButton(
            onClick = { showAddEventSheet = true },
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(16.dp)
        ) {
            Icon(Icons.Default.Add, contentDescription = "Add Event")
        }

        // Add Event Bottom Sheet
        if (showAddEventSheet) {
            AddEventBottomSheet(
                onDismiss = { showAddEventSheet = false },
                onSubmit = { name, date, cost, description ->
                    viewModel.addEvent(name, date, cost, description)
                    showAddEventSheet = false
                }
            )
        }
    }
}

@Composable
fun FilterCard(
        selectedMonth: String,
        onMonthSelected: (String) -> Unit,
        maxCost: String,
        onMaxCostChange: (String) -> Unit,
        applyFilters: () -> Unit
) {
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
                    onMonthSelected = onMonthSelected
                )

                // Max cost input
                OutlinedTextField(
                    value = maxCost,
                    onValueChange = { value -> onMaxCostChange(value.filter { it.isDigit() }) },
                    label = { Text("Max Cost") },
                    modifier = Modifier.fillMaxWidth(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                )

                // Apply filters button
                Button(
                    onClick = applyFilters,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Apply Filters")
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