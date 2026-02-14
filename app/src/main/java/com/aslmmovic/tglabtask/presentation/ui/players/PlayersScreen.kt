package com.aslmmovic.tglabtask.presentation.ui.players


import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.aslmmovic.tglabtask.R
import com.aslmmovic.tglabtask.domain.model.Player
import com.aslmmovic.tglabtask.presentation.ui.component.EmptyView
import com.aslmmovic.tglabtask.presentation.ui.component.ErrorView
import com.aslmmovic.tglabtask.presentation.ui.component.LoadingView
import com.aslmmovic.tglabtask.presentation.util.UiState
import com.aslmmovic.tglabtask.presentation.viewmodel.PlayersViewModel

@Composable
fun PlayersScreen(
    onPlayerTeamClick: (teamId: Int) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: PlayersViewModel = hiltViewModel()
) {
    val query by viewModel.query.collectAsState()
    val state by viewModel.state.collectAsState()

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(stringResource(R.string.search_players), style = MaterialTheme.typography.titleLarge)

        Spacer(Modifier.height(12.dp))

        OutlinedTextField(
            value = query,
            onValueChange = viewModel::onQueryChange,
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            placeholder = { Text(stringResource(R.string.search_placeholder)) }
        )

        Spacer(Modifier.height(12.dp))

        when (state) {
            UiState.Loading -> LoadingView(stringResource(R.string.searching))
            UiState.Empty -> EmptyView(stringResource(R.string.type_to_search_players))
            is UiState.Error -> ErrorView(
                message = (state as UiState.Error).message,
                onRetry = viewModel::retry
            )

            is UiState.Success -> {
                val players = (state as UiState.Success<List<Player>>).data
                LazyColumn(Modifier.fillMaxSize()) {
                    items(players, key = { it.id }) { p ->
                        PlayerRow(
                            player = p,
                            onClick = {
                                p.teamId?.let(onPlayerTeamClick)
                            }
                        )
                        Divider()
                    }
                }
            }
        }
    }
}

@Composable
private fun PlayerRow(player: Player, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(enabled = player.teamId != null) { onClick() }
            .padding(vertical = 12.dp)
    ) {
        Column(Modifier.weight(1f)) {
            Text(
                text = "${player.firstName} ${player.lastName}".trim(),
                style = MaterialTheme.typography.titleMedium
            )
            Spacer(Modifier.height(4.dp))
            Text(player.teamName, style = MaterialTheme.typography.bodyMedium)
        }
    }
}

