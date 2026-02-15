package com.aslmmovic.tglabtask.data.remote.paging


import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.aslmmovic.tglabtask.data.remote.api.ApiConstants.PageSize
import com.aslmmovic.tglabtask.data.remote.api.NbaApi
import com.aslmmovic.tglabtask.data.remote.mapper.toDomain
import com.aslmmovic.tglabtask.domain.model.Game
import retrofit2.HttpException
import java.io.IOException

class GamesPagingSource(
    private val api: NbaApi,
    private val teamId: Int,
    private val perPage: Int = PageSize
) : PagingSource<Int, Game>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Game> {
        val cursor: Int? = params.key // null = first page

        return try {
            val response = api.getGames(teamId = teamId, cursor = cursor, perPage = perPage)
            val games = response.data.map { it.toDomain() }

            LoadResult.Page(
                data = games,
                prevKey = null,
                nextKey = if (games.isEmpty()) null else response.meta?.nextCursor
            )
        } catch (e: IOException) {
            LoadResult.Error(e)
        } catch (e: HttpException) {
            LoadResult.Error(e)
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, Game>): Int? {
        return null
    }
}
