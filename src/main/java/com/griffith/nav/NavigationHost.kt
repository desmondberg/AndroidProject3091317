package com.griffith.nav

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.griffith.screens.JournalScreen
import com.griffith.screens.MapScreen
import com.griffith.screens.SettingsScreen
import com.griffith.viewmodels.GPSLocationViewModel
import com.griffith.viewmodels.JournalViewModel
import com.griffith.viewmodels.SettingsViewModel
import com.griffith.viewmodels.StopwatchViewModel

@Composable
fun NavigationHost(
    controller: NavHostController,
    padding: PaddingValues,
    onThemeChange: (Boolean) -> Unit,
    isDark: Boolean
) {
    //single instances of view models
    val settingsViewModel: SettingsViewModel = viewModel()
    val stopwatchViewModel: StopwatchViewModel = viewModel()
    val journalViewModel: JournalViewModel = viewModel()
    val locationViewModel: GPSLocationViewModel = viewModel()

    NavHost(
        navController = controller,
        startDestination = "map",
        enterTransition = { EnterTransition.None },
        exitTransition = { ExitTransition.None }
    ) {
        composable("map") {
            MapScreen(stopwatchViewModel = stopwatchViewModel, settingsViewModel = settingsViewModel, journalViewModel=journalViewModel, locationViewModel = locationViewModel)
        }
        composable("journal") {
            JournalScreen(journalViewModel = journalViewModel)
        }
        composable("settings") {
            SettingsScreen(
                settingsViewModel = settingsViewModel,
                isDark = isDark,
                onThemeChange = onThemeChange
            )
        }
    }
}
