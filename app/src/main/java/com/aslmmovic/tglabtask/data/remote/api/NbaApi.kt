package com.aslmmovic.tglabtask.data.remote.api

import com.aslmmovic.tglabtask.data.remote.dto.game.GamesResponseDto
import com.aslmmovic.tglabtask.data.remote.dto.player.PlayersResponseDto
import com.aslmmovic.tglabtask.data.remote.dto.team.TeamsResponseDto
import retrofit2.http.GET
import retrofit2.http.Query

interface NbaApi {

    @GET("teams")
    suspend fun getTeams(): TeamsResponseDto

    @GET("games")
    suspend fun getGames(
        @Query("team_ids[]") teamId: Int,
        @Query("page") page: Int,
        @Query("per_page") perPage: Int
    ): GamesResponseDto

    @GET("players")
    suspend fun searchPlayers(
        @Query("search") query: String,
        @Query("page") page: Int,
        @Query("per_page") perPage: Int
    ): PlayersResponseDto


}
