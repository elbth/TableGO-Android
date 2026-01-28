package com.example.tablego.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.Dp
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star

@Composable
fun StarRating(
    rating: Int,
    onRatingSelected: ((Int) -> Unit)? = null,
    starSize: Dp = 24.dp,
    activeColor: Color = MaterialTheme.colorScheme.primary,
    inactiveColor: Color = MaterialTheme.colorScheme.onSurfaceVariant
) {
    Row {
        for (i in 1..5) {
            Icon(
                imageVector = Icons.Default.Star,
                contentDescription = "Star $i",
                tint = if (i <= rating) activeColor else inactiveColor,
                modifier = Modifier
                    .clickable(enabled = onRatingSelected != null) {
                        onRatingSelected?.invoke(i)
                    }
                    .then(Modifier)
            )
        }
    }
}
