package com.aslmmovic.tglabtask.domain.usecase

import android.util.Log
import com.aslmmovic.tglabtask.domain.model.Team
import com.aslmmovic.tglabtask.domain.model.TeamSort
import com.aslmmovic.tglabtask.domain.repository.NbaRepository
import com.aslmmovic.tglabtask.domain.util.AppResult
import jakarta.inject.Inject


class GetTeamsUseCase @Inject constructor(
    private val repo: NbaRepository
) {
    suspend operator fun invoke(sort: TeamSort): AppResult<List<Team>> {
        return when (val result = repo.getTeams()) {
            is AppResult.Success -> {
                val cleaned = result.data
                    .filter { it.city.isNotBlank() && it.fullName.isNotBlank() }
                val sorted = when (sort) {
                    TeamSort.NAME -> cleaned.sortedBy { it.fullName.lowercase() }
                    TeamSort.CITY -> cleaned.sortedBy { it.city.lowercase() }
                    TeamSort.CONFERENCE -> cleaned.sortedBy { it.conference.lowercase() }
                }
                if (sorted.isEmpty()) AppResult.Empty else AppResult.Success(sorted)
            }

            is AppResult.Error -> result
            AppResult.Empty -> AppResult.Empty
        }
    }
}

