package com.aslmmovic.tglabtask.domain.util


sealed class AppError {
    data object NetworkUnavailable : AppError()
    data object Timeout : AppError()
    data class Http(val code: Int, val message: String? = null) : AppError()
    data class Unknown(val message: String? = null) : AppError()
}
