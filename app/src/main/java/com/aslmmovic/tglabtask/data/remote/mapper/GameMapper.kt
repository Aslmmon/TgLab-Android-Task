package com.aslmmovic.tglabtask.data.remote.mapper


import com.aslmmovic.tglabtask.data.remote.dto.game.GameDto
import com.aslmmovic.tglabtask.domain.model.Game

fun GameDto.toDomain(): Game = Game(
    id = id,
    homeTeamName = homeTeam?.fullName ?: "Unknown",
    homeTeamScore = homeTeamScore ?: 0,
    visitorTeamName = visitorTeam?.fullName ?: "Unknown",
    visitorTeamScore = visitorTeamScore ?: 0
)
