package com.aslmmovic.tglabtask.presentation.util


import com.aslmmovic.tglabtask.domain.util.AppError
import com.aslmmovic.tglabtask.domain.util.AppResult

fun <T> AppResult<T>.toUiState(): UiState<T> = when (this) {
    is AppResult.Success -> UiState.Success(data)
    is AppResult.Empty -> UiState.Empty
    is AppResult.Error -> UiState.Error(error.toUserMessage())
}

// for now hardcoded , needed to be extracted to be in resource file
fun AppError.toUserMessage(): String = when (this) {
    AppError.NetworkUnavailable -> "No internet connection."
    AppError.Timeout -> "Request timed out. Please try again."
    is AppError.Http -> when (code) {
        400 -> "Bad request. Please try again."
        401 -> "Unauthorized. Check your API key / access tier."
        404 -> "Not found."
        406 -> "Not acceptable. Please try again."
        429 -> "Rate limited (429). Please wait a moment and retry."
        in 500..599 -> "Server is having issues. Try again later."
        else -> "Request failed (${code}). ${message.orEmpty()}".trim()
    }

    is AppError.Unknown -> message ?: "Something went wrong."
}
