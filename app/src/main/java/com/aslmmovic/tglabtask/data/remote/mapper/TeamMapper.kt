package com.aslmmovic.tglabtask.data.remote.mapper

import com.aslmmovic.tglabtask.data.remote.dto.team.TeamDto
import com.aslmmovic.tglabtask.domain.model.Team

fun TeamDto.toDomain() = Team(
    id = id,
    fullName = fullName ?: name ?: "Unknown",
    city = city ?: "Unknown",
    conference = conference ?: "Unknown"
)
