package com.example.tablego.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.text.input.KeyboardType

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddEventBottomSheet(
    onDismiss: () -> Unit,
    onSubmit: (name: String, date: String, cost: Int, description: String) -> Unit
) {
    var name by remember { mutableStateOf("") }
    var date by remember { mutableStateOf("") }
    var cost by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }


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
                text = "Add Event",
                style = MaterialTheme.typography.titleLarge
            )

            // Title input
            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                label = { Text("Name") },
                modifier = Modifier.fillMaxWidth()
            )

            // Title input
            OutlinedTextField(
                value = date,
                onValueChange = { date = it },
                label = { Text("Date") },
                modifier = Modifier.fillMaxWidth()
            )

            // Body input
            OutlinedTextField(
                value = cost,
                onValueChange = { cost = it },
                label = { Text("Cost") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier
                    .fillMaxWidth()
            )

            // Body input
            OutlinedTextField(
                value = description,
                onValueChange = { description = it },
                label = { Text("Description") },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp)
            )

            // Submit button
            Button(
                onClick = {
                    if (name.isNotBlank() && date.isNotBlank() && cost.isNotBlank() && description
                        .isNotBlank()) {
                        val costInt = cost.toInt()
                        onSubmit(name, date, costInt, description)
                        // reset fields if needed
                        name = ""
                        date = ""
                        cost = ""
                        description = ""
                        onDismiss()
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Submit Event")
            }
        }
    }
}
