package com.griffith.nav

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.griffith.screens.JournalScreen
import com.griffith.screens.MapScreen
import com.griffith.screens.SettingsScreen

@Composable
fun NavigationHost(controller:NavHostController,
                   padding: PaddingValues,
                   onThemeChange: (Boolean) -> Unit,
                   isDark: Boolean){
    NavHost(
        navController = controller,
        startDestination = "map",
        //remove default transitions
        enterTransition = {
            EnterTransition.None
        },
        exitTransition= {
            ExitTransition.None
        }
    ) {
        //pass each screen to the navigation graph
        composable("map") { MapScreen() }
        composable("journal") { JournalScreen() }
        //pass extra parameters to settings to allow it to toggle light/dark mode
        composable("settings") {
            SettingsScreen(isDark= isDark, onThemeChange = onThemeChange)
        }
    }
}