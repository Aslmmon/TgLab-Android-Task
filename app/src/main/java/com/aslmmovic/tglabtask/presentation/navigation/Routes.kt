package com.aslmmovic.tglabtask.presentation.navigation


sealed class Route(val route: String) {
    data object Home : Route("home")
    data object Players : Route("players")
}

