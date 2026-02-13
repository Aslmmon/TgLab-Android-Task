package com.aslmmovic.tglabtask.data.remote.dto.team
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class TeamDto(
    val id: Int,
    val city: String? = null,
    val name: String? = null,
    @SerialName("full_name") val fullName: String? = null,
    val conference: String? = null,
    val division: String? = null,
    val abbreviation: String? = null
)
