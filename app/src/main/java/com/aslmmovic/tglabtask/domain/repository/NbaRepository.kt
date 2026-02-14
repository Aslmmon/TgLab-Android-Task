package com.aslmmovic.tglabtask.domain.repository

import androidx.paging.PagingData
import com.aslmmovic.tglabtask.domain.model.Game
import com.aslmmovic.tglabtask.domain.model.Player
import com.aslmmovic.tglabtask.domain.model.Team
import com.aslmmovic.tglabtask.domain.util.AppResult
import kotlinx.coroutines.flow.Flow


interface NbaRepository {
    suspend fun getTeams(): AppResult<List<Team>>

    fun getTeamGames(teamId: Int): Flow<PagingData<Game>>

    suspend fun searchPlayers(query: String): AppResult<List<Player>>

}
