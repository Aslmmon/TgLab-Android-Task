package com.aslmmovic.tglabtask.presentation.ui.team

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import com.aslmmovic.tglabtask.R
import com.aslmmovic.tglabtask.domain.model.Game
import com.aslmmovic.tglabtask.domain.model.Team
import com.aslmmovic.tglabtask.domain.util.toAppError
import com.aslmmovic.tglabtask.presentation.theme.Dimens
import com.aslmmovic.tglabtask.presentation.ui.component.ErrorView
import com.aslmmovic.tglabtask.presentation.ui.component.LoadingView
import com.aslmmovic.tglabtask.presentation.util.toUserMessage
import com.aslmmovic.tglabtask.presentation.viewmodel.TeamGamesViewModel

@Composable
fun TeamGamesSheet(
    team: Team,
    onClose: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: TeamGamesViewModel = hiltViewModel()
) {
    LaunchedEffect(team) { viewModel.setTeamId(team.id) }

    val games = viewModel.gamesFlow.collectAsLazyPagingItems()

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = Dimens.ScreenPadding)
            .padding(top = Dimens.ScreenPadding)
    ) {
        TeamTopBar(
            title = team.fullName,
            onBack = onClose
        )

        Spacer(Modifier.height(Dimens.SectionSpacing))

        GamesTableHeader()
        HorizontalDivider(color = MaterialTheme.colorScheme.outline.copy(alpha = 0.55f))

        when (val refresh = games.loadState.refresh) {
            is LoadState.Loading -> {
                Spacer(Modifier.height(Dimens.SectionSpacing))
                LoadingView(stringResource(R.string.loading_games))
            }

            is LoadState.Error -> {
                Spacer(Modifier.height(Dimens.SectionSpacing))
                ErrorView(
                    message = refresh.error.toAppError().toUserMessage(),
                    onRetry = games::retry,
                    retryText = stringResource(R.string.retry)
                )
            }

            else -> {
                LazyColumn(
                    modifier = Modifier.fillMaxWidth(),
                    contentPadding = PaddingValues(bottom = Dimens.ScreenPadding)
                ) {
                    items(games.itemCount) { index ->
                        games[index]?.let { game ->
                            GameTableRow(game = game)
                            HorizontalDivider(color = MaterialTheme.colorScheme.outline.copy(alpha = 0.35f))
                        }
                    }

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
private fun TeamTopBar(
    title: String,
    onBack: () -> Unit
) {
    // Layout matches: "< Home" left, team centered
    Box(modifier = Modifier.fillMaxWidth()) {
        TextButton(
            onClick = onBack,
            modifier = Modifier.align(Alignment.CenterStart)
        ) {
            Text(text = stringResource(R.string.back_home))
        }

        Text(
            text = title,
            style = MaterialTheme.typography.titleMedium,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier
                .align(Alignment.Center)
                .padding(horizontal = 72.dp) // keeps center title from colliding with back button
        )
    }
}

@Composable
private fun GamesTableHeader() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = Dimens.TableHeaderVerticalPadding),
        verticalAlignment = Alignment.CenterVertically
    ) {
        HeaderCell(
            text = stringResource(R.string.home_name),
            modifier = Modifier.weight(Dimens.HomeNameWeight),

            )
        HeaderCell(
            text = stringResource(R.string.home_score),
            modifier = Modifier.weight(Dimens.HomeScoreWeight)
        )
        HeaderCell(
            text = stringResource(R.string.visitor_name),
            modifier = Modifier.weight(Dimens.VisitorNameWeight)
        )
        HeaderCell(
            text = stringResource(R.string.visitor_score),
            modifier = Modifier.weight(Dimens.VisitorScoreWeight)
        )
    }
}

@Composable
private fun HeaderCell(
    text: String,
    modifier: Modifier,
) {
    Text(
        text = text,
        modifier = modifier,
        style = MaterialTheme.typography.labelSmall,
        color = MaterialTheme.colorScheme.onSurfaceVariant,
        textAlign = TextAlign.Center,
        overflow = TextOverflow.Ellipsis
    )
}

@Composable
private fun GameTableRow(game: Game) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = Dimens.TableRowVerticalPadding),
        verticalAlignment = Alignment.CenterVertically
    ) {
        BodyCell(
            text = game.homeTeamName,
            modifier = Modifier.weight(Dimens.HomeNameWeight),
        )
        BodyCell(
            text = game.homeTeamScore?.toString() ?: "-",
            modifier = Modifier.weight(Dimens.HomeScoreWeight),
            alignEnd = true
        )
        BodyCell(
            text = game.visitorTeamName,
            modifier = Modifier.weight(Dimens.VisitorNameWeight)
        )
        BodyCell(
            text = game.visitorTeamScore.toString() ?: "-",
            modifier = Modifier.weight(Dimens.VisitorScoreWeight),
            alignEnd = true
        )
    }
}

@Composable
private fun BodyCell(
    text: String,
    modifier: Modifier,
    alignEnd: Boolean = false
) {
    Text(
        text = text,
        modifier = modifier,
        style = MaterialTheme.typography.bodySmall,
        maxLines = 3,
        textAlign = TextAlign.Center,
        overflow = TextOverflow.Ellipsis
    )
}

@Composable
private fun PagingLoadingItem() {
    Box(
        Modifier
            .fillMaxWidth()
            .padding(16.dp),
        contentAlignment = Alignment.Center
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
        TextButton(onClick = onRetry) { Text(stringResource(R.string.retry)) }
    }
}
