package com.example.tablego

import EventListViewModelFactory
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.tablego.data.EventRepository
import com.example.tablego.data.RoomEventRepository
import com.example.tablego.data.local.AppDatabase
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
    val context = LocalContext.current


    // Create the Room repository
    val roomRepository = RoomEventRepository(
        AppDatabase.getInstance(context).eventDao()
    )

    // NavHost holds all composable destinations (screens)
    NavHost(navController = navController, startDestination = "event_list") {
        // composable("route") = each screen is a route and can take arguments
        composable("event_list") {
            val viewModel: EventListViewModel = viewModel(
                factory = EventListViewModelFactory(roomRepository)
            )
            EventListScreen(
                viewModel,
                onEventClick = { eventId ->
                    // go to detail screen and pass ID
                    navController.navigate("event_detail/$eventId")
                }
            )
        }
        composable("event_detail/{eventId}") { backStackEntry ->
            // retrieve parameters for destination screen AKA event ID
            val eventId = backStackEntry.arguments
                ?.getString("eventId")
                ?.toInt() ?: 0
            // Handle event details screen
            EventDetailScreen(
                eventId = eventId,
                onBack = { navController.popBackStack() }
            )
        }
    }

}