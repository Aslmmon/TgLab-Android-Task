package com.aslmmovic.tglabtask.data.remote.paging


import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.aslmmovic.tglabtask.data.remote.api.NbaApi
import com.aslmmovic.tglabtask.data.remote.mapper.toDomain
import com.aslmmovic.tglabtask.domain.model.Game
import retrofit2.HttpException
import java.io.IOException

class GamesPagingSource(
    private val api: NbaApi,
    private val teamId: Int,
    private val perPage: Int = 25
) : PagingSource<Int, Game>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Game> {
        val page = params.key ?: 1

        return try {
            val response = api.getGames(teamId = teamId, page = page, perPage = perPage)
            val games = response.data.map { it.toDomain() }

            LoadResult.Page(
                data = games,
                prevKey = if (page == 1) null else page - 1,
                nextKey = if (games.isEmpty()) null else page + 1
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
        val anchor = state.anchorPosition ?: return null
        val page = state.closestPageToPosition(anchor) ?: return null
        return page.prevKey?.plus(1) ?: page.nextKey?.minus(1)
    }
}
