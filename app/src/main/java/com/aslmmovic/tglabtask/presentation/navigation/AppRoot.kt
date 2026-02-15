package com.aslmmovic.tglabtask.presentation.navigation


import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.*
import com.aslmmovic.tglabtask.domain.model.Team
import com.aslmmovic.tglabtask.presentation.ui.home.HomeScreen
import com.aslmmovic.tglabtask.presentation.ui.players.PlayersScreen
import com.aslmmovic.tglabtask.presentation.ui.team.TeamGamesSheet

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppRoot() {
    val navController = rememberNavController()
    val navBackStackEntry = navController.currentBackStackEntryAsState().value
    val currentRoute = navBackStackEntry?.destination?.route
    var selectedTeamId by remember { mutableStateOf<Team?>(null) }
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    Scaffold(
        bottomBar = {
            NavigationBar {
                bottomNavItems.forEach { item ->
                    val selected = currentRoute == item.route
                    NavigationBarItem(
                        selected = selected,
                        onClick = {
                            navController.navigate(item.route) {
                                popUpTo(navController.graph.findStartDestination().id) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        },
                        icon = { Icon(item.icon, contentDescription = item.label) },
                        label = { Text(item.label) }
                    )
                }
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Route.Home.route,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(Route.Home.route) {
                HomeScreen(
                    onTeamClick = { teamId -> selectedTeamId = teamId }
                )
            }

            composable(Route.Players.route) {
//                PlayersScreen(
//                    onPlayerTeamClick = { teamId -> selectedTeamId = teamId }
//                )
            }
        }
    }
    if (selectedTeamId != null) {
        ModalBottomSheet(
            onDismissRequest = { selectedTeamId = null },
            sheetState = sheetState
        ) {
            selectedTeamId?.let {
                TeamGamesSheet(
                    team = it,
                    onClose = { selectedTeamId = null }
                )
            }
        }
    }
}
