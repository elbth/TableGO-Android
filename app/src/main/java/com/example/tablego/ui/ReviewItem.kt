package com.example.tablego.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.tablego.data.Review

@Composable
fun ReviewItem(review: Review) {
    Column {
        Text(
            text = review.title,
            style = MaterialTheme.typography.titleSmall
        )

        Spacer(modifier = Modifier.height(4.dp))

        StarRating(
            rating = review.rating,
            onRatingSelected = null // display only, no click here
        )

        Spacer(modifier = Modifier.height(4.dp))

        Text(
            text = review.body,
            style = MaterialTheme.typography.bodyMedium
        )

        Spacer(modifier = Modifier.height(8.dp))
    }
}
