package com.aslmmovic.tglabtask.data.remote.dto.game

import com.aslmmovic.tglabtask.data.remote.dto.common.MetaDto

import kotlinx.serialization.Serializable

@Serializable
data class GamesResponseDto(
    val data: List<GameDto> = emptyList(),
    val meta: MetaDto? = null
)
