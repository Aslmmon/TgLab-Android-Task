package com.aslmmovic.tglabtask.presentation.util


import com.aslmmovic.tglabtask.domain.util.AppError
import com.aslmmovic.tglabtask.domain.util.AppResult

fun <T> AppResult<T>.toUiState(): UiState<T> = when (this) {
    is AppResult.Success -> UiState.Success(data)
    is AppResult.Empty -> UiState.Empty
    is AppResult.Error -> UiState.Error(error.toUserMessage())
}

 fun AppError.toUserMessage(): String = when (this) {
    AppError.NetworkUnavailable -> "No internet connection."
    AppError.Timeout -> "Request timed out. Please try again."
    is AppError.Http -> "Server error (${code}). ${message.orEmpty()}".trim()
    is AppError.Unknown -> message ?: "Something went wrong."
}
