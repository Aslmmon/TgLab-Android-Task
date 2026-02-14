package com.aslmmovic.tglabtask.presentation.ui.team

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import com.aslmmovic.tglabtask.R
import com.aslmmovic.tglabtask.domain.model.Game
import com.aslmmovic.tglabtask.domain.util.toAppError
import com.aslmmovic.tglabtask.presentation.theme.Dimens
import com.aslmmovic.tglabtask.presentation.ui.component.ErrorView
import com.aslmmovic.tglabtask.presentation.ui.component.LoadingView
import com.aslmmovic.tglabtask.presentation.util.UiState
import com.aslmmovic.tglabtask.presentation.util.toUserMessage
import com.aslmmovic.tglabtask.presentation.viewmodel.TeamGamesViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TeamGamesSheet(
    teamId: Int,
    onClose: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: TeamGamesViewModel = hiltViewModel()
) {

    LaunchedEffect(teamId) {
        viewModel.setTeamId(teamId)
    }
    val games = viewModel.gamesFlow.collectAsLazyPagingItems()


    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(Dimens.ScreenPadding)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("Recent Games", style = MaterialTheme.typography.titleLarge)
            Spacer(Modifier.weight(1f))
            TextButton(onClick = onClose) { Text("Close") }
        }

        Spacer(Modifier.height(Dimens.ItemSpacing))

        when (val refresh = games.loadState.refresh) {
            is LoadState.Loading ->
                LoadingView(stringResource(R.string.loading_teams))

            is LoadState.Error -> ErrorView(
                message = refresh.error.toAppError().toUserMessage(),
                onRetry = games::retry
            )

            else -> {
                LazyColumn(
                    modifier = Modifier.fillMaxWidth(),
                    contentPadding = PaddingValues(bottom = Dimens.ScreenPadding)
                ) {
                    items(games.itemCount) { index ->
                        games[index]?.let { GameRow(it) }
                        Divider()
                    }

                    // append (pagination) state
                    when (val append = games.loadState.append) {
                        is LoadState.Loading -> item { PagingLoadingItem() }
                        is LoadState.Error -> item {
                            PagingErrorItem(
                                message = append.error.message
                                    ?: stringResource(R.string.failed_to_load_more),
                                onRetry = { games.retry() }
                            )
                        }

                        else -> Unit
                    }
                }
            }
        }
    }
}

@Composable
private fun GameRow(game: Game) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = Dimens.ItemSpacing)
    ) {
        Text(
            text = "${game.homeTeamName}  ${game.homeTeamScore}",
            style = MaterialTheme.typography.titleMedium
        )
        Text(
            text = "${game.visitorTeamName}  ${game.visitorTeamScore}",
            style = MaterialTheme.typography.bodyLarge
        )
    }
}


@Composable
private fun PagingLoadingItem() {
    Box(
        Modifier
            .fillMaxWidth()
            .padding(16.dp), contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator()
    }
}

@Composable
private fun PagingErrorItem(message: String, onRetry: () -> Unit) {
    Row(
        Modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(message, modifier = Modifier.weight(1f))
        TextButton(onClick = onRetry) { Text("Retry") }
    }
}
