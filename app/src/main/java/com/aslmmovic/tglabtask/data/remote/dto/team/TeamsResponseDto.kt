package com.aslmmovic.tglabtask.data.remote.dto.team


import com.aslmmovic.tglabtask.data.remote.dto.common.MetaDto
import kotlinx.serialization.Serializable

@Serializable
data class TeamsResponseDto(
    val data: List<TeamDto> = emptyList(),
    val meta: MetaDto? = null
)
