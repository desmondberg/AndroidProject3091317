package com.griffith.nav

import com.griffith.models.NavItem
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.*


object NavDestinations{
    val BottomBarDestinations = listOf(
        NavItem(name="My Journal",
            route="journal",
            icon= Icons.AutoMirrored.Filled.List
        ),
        NavItem(name="Map",
            route="map",
            icon=Icons.Filled.Place
        ),
        NavItem(name="Settings",
            route="settings",
            icon=Icons.Filled.Settings
        )
    )
}