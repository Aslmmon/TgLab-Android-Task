package com.aslmmovic.tglabtask.data.remote
import com.aslmmovic.tglabtask.domain.util.AppError
import com.aslmmovic.tglabtask.domain.util.AppResult
import kotlinx.coroutines.TimeoutCancellationException
import retrofit2.HttpException
import java.io.IOException

suspend fun <T> safeApiCall(block: suspend () -> T): AppResult<T> {
    return try {
        AppResult.Success(block())
    } catch (e: IOException) { // no internet, DNS, etc
        AppResult.Error(AppError.NetworkUnavailable)
    } catch (e: TimeoutCancellationException) {
        AppResult.Error(AppError.Timeout)
    } catch (e: HttpException) {
        AppResult.Error(AppError.Http(e.code(), e.message()))
    } catch (e: Exception) {
        AppResult.Error(AppError.Unknown(e.message))
    }
}
