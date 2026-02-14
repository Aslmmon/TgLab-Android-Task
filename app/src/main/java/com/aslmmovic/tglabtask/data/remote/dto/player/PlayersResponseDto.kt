package com.aslmmovic.tglabtask.data.remote.dto.player


import com.aslmmovic.tglabtask.data.remote.dto.common.MetaDto
import kotlinx.serialization.Serializable


@Serializable
data class PlayersResponseDto(
    val data: List<PlayerDto> = emptyList()
)
