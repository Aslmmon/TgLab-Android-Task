package com.aslmmovic.tglabtask.data.remote.dto.player

import com.aslmmovic.tglabtask.data.remote.dto.team.TeamDto


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PlayerDto(
    val id: Int,
    @SerialName("first_name") val firstName: String? = null,
    @SerialName("last_name") val lastName: String? = null,
    val position: String? = null,
    val team: TeamDto? = null
)
