package com.aslmmovic.tglabtask.domain.model


data class Game(
    val id: Int,
    val homeTeamName: String,
    val homeTeamScore: Int,
    val visitorTeamName: String,
    val visitorTeamScore: Int
)
