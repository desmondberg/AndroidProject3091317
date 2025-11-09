package com.griffith.models
import androidx.compose.ui.graphics.vector.ImageVector

// a NavItem provides a name, route and icon to a navigation destination
data class NavItem(
    val name: String,
    val route: String,
    val icon: ImageVector
)