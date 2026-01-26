package com.example.tablego

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.tablego.ui.EventDetailScreen
import com.example.tablego.ui.EventListScreen
import com.example.tablego.ui.EventListViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            MaterialTheme {
                AppNavHost()
            }
        }
    }
}

@Composable
// central navigation host
fun AppNavHost() {
    // rememberNavController() = keeps navigation state
    val navController = rememberNavController()

    // NavHost holds all composable destinations (screens)
    NavHost(navController = navController, startDestination = "event_list") {
        // composable("route") = each screen is a route and can take arguments
        composable("event_list") {
            EventListScreen(
                viewModel = EventListViewModel(),
                onEventClick = { eventId ->
                    // go to detail screen and pass ID
                    navController.navigate("event_detail/$eventId")
                }
            )
        }
        composable("event_detail/{eventId}") { backStackEntry ->
            // retrieve parameters for destination screen AKA event ID
            val eventId = backStackEntry.arguments?.getString("eventId")?.toInt() ?: 0
            // Handle event details screen
            EventDetailScreen(eventId = eventId)
        }
    }

}