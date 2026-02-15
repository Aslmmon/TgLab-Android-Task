package com.aslmmovic.tglabtask.presentation.ui.home

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Button
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.aslmmovic.tglabtask.R
import com.aslmmovic.tglabtask.domain.model.Team
import com.aslmmovic.tglabtask.domain.model.TeamSort
import com.aslmmovic.tglabtask.domain.model.toButtonTitle
import com.aslmmovic.tglabtask.presentation.theme.Dimens
import com.aslmmovic.tglabtask.presentation.ui.component.EmptyView
import com.aslmmovic.tglabtask.presentation.ui.component.ErrorView
import com.aslmmovic.tglabtask.presentation.ui.component.LoadingView
import com.aslmmovic.tglabtask.presentation.util.UiState
import com.aslmmovic.tglabtask.presentation.viewmodel.HomeViewModel
import androidx.compose.ui.res.stringResource

@Composable
fun HomeScreen(
    onTeamClick: (team: Team) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: HomeViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()
    val sort by viewModel.sort.collectAsState()

    var showSortDialog by remember { mutableStateOf(false) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(Dimens.ScreenPadding)
    ) {
        // Top bar: "Home" + pill sort button
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = stringResource(R.string.home), // add "home" string if needed
                style = MaterialTheme.typography.headlineSmall
            )

            PillSortButton(
                title = sort.toButtonTitle(),
                onClick = { showSortDialog = true }
            )
        }

        Spacer(modifier = Modifier.height(Dimens.ItemSpacing))

        when (state) {
            UiState.Loading ->
                LoadingView(stringResource(R.string.loading_teams))

            UiState.Empty ->
                EmptyView(stringResource(R.string.no_teams_found))

            is UiState.Error ->
                ErrorView(
                    message = (state as UiState.Error).message,
                    onRetry = viewModel::retry,
                    retryText = stringResource(R.string.retry)
                )

            is UiState.Success -> {
                val teams = (state as UiState.Success<List<Team>>).data
                TeamsTable(
                    teams = teams,
                    onTeamClick = onTeamClick
                )
            }
        }
    }

    if (showSortDialog) {
        SortDialog(
            selected = sort,
            onSelect = {
                viewModel.onSortSelected(it)
                showSortDialog = false
            },
            onDismiss = { showSortDialog = false }
        )
    }
}

@Composable
private fun PillSortButton(
    title: String,
    onClick: () -> Unit
) {
    // Matches the small rounded pill in the design
    Button(
        onClick = onClick,
        shape = RoundedCornerShape(20),
        contentPadding = PaddingValues(
            horizontal = 14.dp,
            vertical = 6.dp
        ),
        colors = ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant,
            contentColor = MaterialTheme.colorScheme.onSurfaceVariant
        ),
        elevation = ButtonDefaults.buttonElevation(defaultElevation = 0.dp)
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.labelLarge
        )
    }
}

@Composable
private fun TeamsTable(
    teams: List<Team>,
    onTeamClick: (Team) -> Unit
) {
    Column(modifier = Modifier.fillMaxSize()) {
        TableHeaderRow()
        HorizontalDivider()

        LazyColumn(modifier = Modifier.fillMaxSize()) {
            items(
                items = teams,
                key = { it.id }
            ) { team ->
                TeamTableRow(
                    team = team,
                    onClick = { onTeamClick(team) }
                )
            }
        }
    }
}

@Composable
private fun TableHeaderRow() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        HeaderCell(
            text = stringResource(R.string.name),
            modifier = Modifier.weight(Dimens.NameWeight)
        )
        HeaderCell(
            text = stringResource(R.string.city),
            modifier = Modifier.weight(Dimens.CityWeight)
        )
        HeaderCell(
            text = stringResource(R.string.conference),
            modifier = Modifier.weight(Dimens.ConferenceWeight)
        )

        // space where chevron column lives
        Spacer(modifier = Modifier.width(24.dp))
    }
}

@Composable
private fun HeaderCell(
    text: String,
    modifier: Modifier = Modifier
) {
    Text(
        text = text,
        modifier = modifier,
        style = MaterialTheme.typography.labelLarge,
        color = MaterialTheme.colorScheme.onSurfaceVariant,
        maxLines = 1,
        overflow = TextOverflow.Ellipsis
    )
}

@Composable
private fun TeamTableRow(
    team: Team,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(vertical = 14.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {

        BodyCell(
            text = team.fullName,
            modifier = Modifier.weight(Dimens.NameWeight)
        )

        BodyCell(
            text = team.city,
            modifier = Modifier.weight(Dimens.CityWeight)
        )

        BodyCell(
            text = team.conference,
            modifier = Modifier.weight(Dimens.ConferenceWeight)
        )

        Icon(
            imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
            contentDescription = null
        )
    }
}


@Composable
private fun BodyCell(
    text: String,
    modifier: Modifier = Modifier
) {
    Text(
        text = text,
        modifier = modifier,
        style = MaterialTheme.typography.bodySmall,
        maxLines = 1,
        overflow = TextOverflow.Clip
    )
}


@Composable
private fun SortDialog(
    selected: TeamSort,
    onSelect: (TeamSort) -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(stringResource(R.string.sort_by)) },
        text = {
            Column {
                SortOptionRow(
                    title = TeamSort.NAME.toButtonTitle(),
                    selected = selected == TeamSort.NAME,
                    onClick = { onSelect(TeamSort.NAME) }
                )
                SortOptionRow(
                    title = TeamSort.CITY.toButtonTitle(),
                    selected = selected == TeamSort.CITY,
                    onClick = { onSelect(TeamSort.CITY) }
                )
                SortOptionRow(
                    title = TeamSort.CONFERENCE.toButtonTitle(),
                    selected = selected == TeamSort.CONFERENCE,
                    onClick = { onSelect(TeamSort.CONFERENCE) }
                )
            }
        },
        confirmButton = {
            TextButton(onClick = onDismiss) { Text(stringResource(R.string.close)) }
        }
    )
}

@Composable
private fun SortOptionRow(
    title: String,
    selected: Boolean,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(vertical = Dimens.ItemSpacing),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = if (selected) "✓ $title" else title,
            style = MaterialTheme.typography.bodyLarge
        )
    }
}
