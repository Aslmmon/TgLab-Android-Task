package com.aslmmovic.tglabtask.data.remote.dto.game


import com.aslmmovic.tglabtask.data.remote.dto.team.TeamDto
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


@Serializable
data class GameDto(
    val id: Int,
    @SerialName("home_team_score") val homeTeamScore: Int? = null,
    @SerialName("visitor_team_score") val visitorTeamScore: Int? = null,
    @SerialName("home_team") val homeTeam: TeamDto? = null,
    @SerialName("visitor_team") val visitorTeam: TeamDto? = null
)