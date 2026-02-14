package com.aslmmovic.tglabtask.domain.util

import okio.IOException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import retrofit2.HttpException


sealed class AppError {
    data object NetworkUnavailable : AppError()
    data object Timeout : AppError()
    data class Http(val code: Int, val message: String? = null) : AppError()
    data class Unknown(val message: String? = null) : AppError()
}


fun Throwable.toAppError(): AppError = when (this) {
    is UnknownHostException -> AppError.NetworkUnavailable
    is SocketTimeoutException -> AppError.Timeout
    is IOException -> AppError.NetworkUnavailable // covers no-internet / connection reset etc
    is HttpException -> AppError.Http(code(), message())
    else -> AppError.Unknown(message)
}