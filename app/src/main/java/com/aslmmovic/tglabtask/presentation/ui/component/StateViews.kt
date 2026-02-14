package com.aslmmovic.tglabtask.presentation.ui.component


import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.aslmmovic.tglabtask.R
import com.aslmmovic.tglabtask.presentation.theme.Dimens

@Composable
fun LoadingView(
    text: String,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        CircularProgressIndicator()
        Spacer(Modifier.height(Dimens.ItemSpacing))
        Text(text)
    }
}

@Composable
fun EmptyView(
    text: String,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text(text)
    }
}

@Composable
fun ErrorView(
    message: String,
    onRetry: () -> Unit,
    retryText: String = stringResource(R.string.retry),
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(message)
        Spacer(Modifier.height(Dimens.ItemSpacing))
        Button(onClick = onRetry) { Text(retryText) }
    }
}
