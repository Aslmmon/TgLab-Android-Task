package com.aslmmovic.tglabtask.data.remote.mapper


import com.aslmmovic.tglabtask.data.remote.dto.player.PlayerDto
import com.aslmmovic.tglabtask.domain.model.Player

fun PlayerDto.toDomain(): Player = Player(
    id = id,
    firstName = firstName ?: "",
    lastName = lastName ?: "",
    teamId = team?.id,
    teamName = team?.fullName ?: "Unknown"
)
