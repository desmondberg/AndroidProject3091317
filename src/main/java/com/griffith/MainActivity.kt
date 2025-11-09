package com.griffith


import android.os.Bundle
import androidx.activity.*
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.*
import androidx.compose.ui.graphics.*
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.griffith.models.NavItem
import com.griffith.nav.NavDestinations
import com.griffith.nav.NavigationHost
import com.griffith.ui.theme.MyAppTheme


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            var isDark by rememberSaveable { mutableStateOf(false) }

            MyAppTheme(darkTheme = isDark) {
                val navController = rememberNavController()
                Surface(color = if (isDark) Color.Black else Color.White) {
                    Scaffold(
                        bottomBar = { BottomNavigationBar(controller= navController) },
                        content = { padding ->
                            NavigationHost(
                                controller = navController,
                                padding = padding,
                                onThemeChange = { isDark = it },
                                isDark = isDark
                            )
                        }
                    )
                }
            }
        }

    }
}

@Composable
fun BottomNavigationBar(controller: NavHostController){
    val startDestination = NavDestinations.BottomBarDestinations.find { it.route == "map" }
    var currentDestination by rememberSaveable {mutableStateOf(startDestination?.route ?: "map")}
    NavigationBar {
        NavDestinations.BottomBarDestinations.forEachIndexed { index, destination->
            NavigationBarItem(
                selected = currentDestination == destination.route,
                onClick = {
                    currentDestination= destination.route
                    controller.navigate(destination.route)
                },
                icon = { Icon(destination.icon, contentDescription = destination.name) },
                label = { Text(destination.name) }
            )
        }
    }

}

