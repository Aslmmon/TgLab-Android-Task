package com.aslmmovic.tglabtask.data.remote.repository


import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.aslmmovic.tglabtask.data.remote.api.NbaApi
import com.aslmmovic.tglabtask.data.remote.mapper.toDomain
import com.aslmmovic.tglabtask.data.remote.paging.GamesPagingSource
import com.aslmmovic.tglabtask.data.remote.safeApiCall
import com.aslmmovic.tglabtask.domain.model.Game
import com.aslmmovic.tglabtask.domain.model.Player
import com.aslmmovic.tglabtask.domain.model.Team
import com.aslmmovic.tglabtask.domain.repository.NbaRepository
import com.aslmmovic.tglabtask.domain.util.AppResult
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject


class NbaRepositoryImpl @Inject constructor(
    private val api: NbaApi
) : NbaRepository {


    override suspend fun getTeams(): AppResult<List<Team>> = safeApiCall {
        api.getTeams().data.map { it.toDomain() }
    }.let { result ->
        when (result) {
            is AppResult.Success -> if (result.data.isEmpty()) AppResult.Empty else result
            else -> result
        }
    }


    override fun getTeamGames(teamId: Int): Flow<PagingData<Game>> {
        return Pager(
            config = PagingConfig(
                pageSize = 25,
                enablePlaceholders = false
            ),
            pagingSourceFactory = { GamesPagingSource(api, teamId) }
        ).flow
    }


    override suspend fun searchPlayers(query: String): AppResult<List<Player>> = safeApiCall {
        api.searchPlayers(query = query, page = 1, perPage = 25)
            .data
            .map { it.toDomain() }
    }.let { result ->
        when (result) {
            is AppResult.Success -> if (result.data.isEmpty()) AppResult.Empty else result
            else -> result
        }
    }

}
