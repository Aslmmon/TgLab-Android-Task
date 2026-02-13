package com.aslmmovic.tglabtask.data.remote.repository


import com.aslmmovic.tglabtask.data.remote.api.NbaApi
import com.aslmmovic.tglabtask.data.remote.mapper.toDomain
import com.aslmmovic.tglabtask.domain.model.Team
import com.aslmmovic.tglabtask.domain.repository.NbaRepository
import javax.inject.Inject

class NbaRepositoryImpl @Inject constructor(
    private val api: NbaApi
) : NbaRepository {

    override suspend fun getTeams(): List<Team> {
        return api.getTeams().data.map { it.toDomain() }
    }
}
