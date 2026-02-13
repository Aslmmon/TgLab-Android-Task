package com.aslmmovic.tglabtask.domain.repository

import com.aslmmovic.tglabtask.domain.model.Team
import com.aslmmovic.tglabtask.domain.util.AppResult


interface NbaRepository {
    suspend fun getTeams(): AppResult<List<Team>>
}
