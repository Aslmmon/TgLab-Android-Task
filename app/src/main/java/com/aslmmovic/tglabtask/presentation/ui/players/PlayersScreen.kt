package com.aslmmovic.tglabtask.presentation.ui.players

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import com.aslmmovic.tglabtask.R
import com.aslmmovic.tglabtask.domain.model.Player
import com.aslmmovic.tglabtask.domain.model.Team
import com.aslmmovic.tglabtask.presentation.theme.Dimens
import com.aslmmovic.tglabtask.presentation.ui.component.EmptyView
import com.aslmmovic.tglabtask.presentation.ui.component.ErrorView
import com.aslmmovic.tglabtask.presentation.ui.component.LoadingView
import com.aslmmovic.tglabtask.presentation.util.UiState
import com.aslmmovic.tglabtask.presentation.viewmodel.PlayersViewModel
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

@Composable
fun PlayersScreen(
    onPlayerTeamClick: (team: Team) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: PlayersViewModel = hiltViewModel()
) {
    val query by viewModel.query.collectAsState()
    val state by viewModel.state.collectAsState()

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(Dimens.ScreenPadding)
    ) {
        Text(
            text = stringResource(R.string.players),
            style = MaterialTheme.typography.headlineSmall
        )

        Spacer(Modifier.height(Dimens.SectionSpacing))

        PlayersSearchPill(
            value = query,
            onValueChange = viewModel::onQueryChange
        )

        Spacer(Modifier.height(Dimens.SectionSpacing))

        PlayersTableHeader()
        HorizontalDivider(color = MaterialTheme.colorScheme.outline.copy(alpha = 0.55f))

        when (state) {
            UiState.Loading -> {
                Spacer(Modifier.height(Dimens.SectionSpacing))
                LoadingView(stringResource(R.string.searching))
            }

            UiState.Empty -> {
                Spacer(Modifier.height(Dimens.SectionSpacing))
                EmptyView(stringResource(R.string.type_to_search_players))
            }

            is UiState.Error -> {
                Spacer(Modifier.height(Dimens.SectionSpacing))
                ErrorView(
                    message = (state as UiState.Error).message,
                    onRetry = viewModel::retry,
                    retryText = stringResource(R.string.retry)
                )
            }

            is UiState.Success -> {
                val players = (state as UiState.Success<List<Player>>).data

                LazyColumn(modifier = Modifier.fillMaxSize()) {
                    items(players, key = { it.id }) { p ->
                        PlayerTableRow(
                            player = p,
                            onClick = {
                                p.teamData?.let(onPlayerTeamClick)
                            }
                        )
                        HorizontalDivider(color = MaterialTheme.colorScheme.outline.copy(alpha = 0.35f))
                    }
                }
            }
        }
    }
}

@Composable
private fun PlayersSearchPill(
    value: String,
    onValueChange: (String) -> Unit
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = Modifier.fillMaxWidth().defaultMinSize(minHeight = 40.dp),

        placeholder = {
            Text(
                text = stringResource(R.string.search),
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center
            )

        },

        shape = RoundedCornerShape(20),
        colors = OutlinedTextFieldDefaults.colors(
            focusedContainerColor = MaterialTheme.colorScheme.surfaceVariant,
            unfocusedContainerColor = MaterialTheme.colorScheme.surfaceVariant,
            focusedBorderColor = MaterialTheme.colorScheme.surfaceVariant,
            unfocusedBorderColor = MaterialTheme.colorScheme.surfaceVariant,
            focusedTextColor = MaterialTheme.colorScheme.onSurface,
            unfocusedTextColor = MaterialTheme.colorScheme.onSurface
        )
    )
}

@Composable
private fun PlayersTableHeader() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = Dimens.TableHeaderVerticalPadding),
        verticalAlignment = Alignment.CenterVertically
    ) {
        HeaderCell(
            text = stringResource(R.string.first_name),
            modifier = Modifier.weight(Dimens.FirstNameWeight)
        )
        HeaderCell(
            text = stringResource(R.string.last_name),
            modifier = Modifier.weight(Dimens.LastNameWeight)
        )
        HeaderCell(
            text = stringResource(R.string.team),
            modifier = Modifier.weight(Dimens.TeamWeight)
        )

        Spacer(Modifier.width(Dimens.ChevronColumnWidth))
    }
}

@Composable
private fun PlayerTableRow(
    player: Player,
    onClick: () -> Unit
) {
    val enabled = player.teamData != null

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(enabled = enabled) { onClick() }
            .padding(vertical = Dimens.TableRowVerticalPadding),
        verticalAlignment = Alignment.CenterVertically
    ) {
        BodyCell(
            text = player.firstName,
            modifier = Modifier.weight(Dimens.FirstNameWeight)
        )
        BodyCell(
            text = player.lastName,
            modifier = Modifier.weight(Dimens.LastNameWeight)
        )
        BodyCell(
            text = player.teamName,
            modifier = Modifier.weight(Dimens.TeamWeight)
        )

        Icon(
            imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
            contentDescription = null,
            tint = if (enabled)
                MaterialTheme.colorScheme.onSurfaceVariant
            else
                MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.35f)
        )
    }
}

@Composable
private fun HeaderCell(
    text: String,
    modifier: Modifier
) {
    Text(
        text = text,
        modifier = modifier,
        style = MaterialTheme.typography.labelSmall,
        color = MaterialTheme.colorScheme.onSurfaceVariant,
        maxLines = 1,
        overflow = TextOverflow.Ellipsis
    )
}

@Composable
private fun BodyCell(
    text: String,
    modifier: Modifier
) {
    Text(
        text = text,
        modifier = modifier,
        style = MaterialTheme.typography.bodyMedium,
        maxLines = 1,
        overflow = TextOverflow.Ellipsis
    )
}
