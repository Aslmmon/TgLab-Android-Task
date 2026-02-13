package com.aslmmovic.tglabtask.presentation.ui.home


import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.aslmmovic.tglabtask.domain.model.Team
import com.aslmmovic.tglabtask.domain.model.TeamSort
import com.aslmmovic.tglabtask.presentation.util.UiState
import com.aslmmovic.tglabtask.presentation.viewmodel.HomeViewModel

@Composable
fun HomeScreen(
    onTeamClick: (teamId: Int) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: HomeViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()
    val sort by viewModel.sort.collectAsState()

    var showSortDialog by remember { mutableStateOf(false) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Top row: Sort button
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Teams",
                style = MaterialTheme.typography.titleLarge
            )

            Button(onClick = { showSortDialog = true }) {
                Text(text = sort.toButtonTitle())
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        when (state) {
            UiState.Loading -> LoadingView()

            UiState.Empty -> EmptyView(
                message = "No teams found."
            )

            is UiState.Error -> ErrorView(
                message = (state as UiState.Error).message,
                onRetry = viewModel::retry
            )

            is UiState.Success -> {
                val teams = (state as UiState.Success<List<Team>>).data
                TeamsList(
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
private fun TeamsList(
    teams: List<Team>,
    onTeamClick: (Int) -> Unit
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize()
    ) {
        items(
            items = teams,
            key = { it.id }
        ) { team ->
            TeamRow(team = team, onClick = { onTeamClick(team.id) })
            Divider()
        }
    }
}

@Composable
private fun TeamRow(
    team: Team,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = team.fullName,
                style = MaterialTheme.typography.titleMedium
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "${team.city} • ${team.conference}",
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}

@Composable
private fun SortDialog(
    selected: TeamSort,
    onSelect: (TeamSort) -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Sort by") },
        text = {
            Column {
                SortOptionRow(
                    title = "Name",
                    selected = selected == TeamSort.NAME,
                    onClick = { onSelect(TeamSort.NAME) }
                )
                SortOptionRow(
                    title = "City",
                    selected = selected == TeamSort.CITY,
                    onClick = { onSelect(TeamSort.CITY) }
                )
                SortOptionRow(
                    title = "Conference",
                    selected = selected == TeamSort.CONFERENCE,
                    onClick = { onSelect(TeamSort.CONFERENCE) }
                )
            }
        },
        confirmButton = {
            TextButton(onClick = onDismiss) { Text("Close") }
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
            .padding(vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = if (selected) "✓ $title" else title,
            style = MaterialTheme.typography.bodyLarge
        )
    }
}

@Composable
private fun LoadingView() {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        CircularProgressIndicator()
        Spacer(modifier = Modifier.height(10.dp))
        Text("Loading teams...")
    }
}

@Composable
private fun EmptyView(message: String) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(message)
    }
}

@Composable
private fun ErrorView(
    message: String,
    onRetry: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(message)
        Spacer(modifier = Modifier.height(12.dp))
        Button(onClick = onRetry) { Text("Retry") }
    }
}

private fun TeamSort.toButtonTitle(): String = when (this) {
    TeamSort.NAME -> "Name"
    TeamSort.CITY -> "City"
    TeamSort.CONFERENCE -> "Conference"
}
