package com.aslmmovic.tglabtask.presentation.navigation


import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Search
import androidx.compose.ui.graphics.vector.ImageVector

data class BottomNavItem(
    val route: String,
    val label: String,
    val icon: ImageVector
)

val bottomNavItems = listOf(
    BottomNavItem(Route.Home.route, "Home", Icons.Filled.Home),
    BottomNavItem(Route.Players.route, "Players", Icons.Filled.Search)
)
