package com.example.tablego.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddReviewBottomSheet(
    onDismiss: () -> Unit,
    onSubmit: (title: String, rating: Int, body: String) -> Unit
) {
    var title by remember { mutableStateOf("") }
    var body by remember { mutableStateOf("") }
    var rating by remember { mutableStateOf(0) }

    ModalBottomSheet(
        onDismissRequest = onDismiss
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                text = "Add Review",
                style = MaterialTheme.typography.titleLarge
            )

            // Title input
            OutlinedTextField(
                value = title,
                onValueChange = { title = it },
                label = { Text("Title") },
                modifier = Modifier.fillMaxWidth()
            )

            // Star rating selection
            StarRating(
                rating = rating,
                onRatingSelected = { rating = it }
            )

            // Body input
            OutlinedTextField(
                value = body,
                onValueChange = { body = it },
                label = { Text("Review") },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp)
            )

            // Submit button
            Button(
                onClick = {
                    if (title.isNotBlank() && rating > 0) {
                        onSubmit(title, rating, body)
                        // reset fields if needed
                        title = ""
                        body = ""
                        rating = 0
                        onDismiss()
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Submit Review")
            }
        }
    }
}
