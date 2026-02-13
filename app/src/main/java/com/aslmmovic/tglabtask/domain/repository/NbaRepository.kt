package com.aslmmovic.tglabtask.domain.repository

import com.aslmmovic.tglabtask.domain.model.Team


interface NbaRepository {
    suspend fun getTeams(): List<Team>
}
