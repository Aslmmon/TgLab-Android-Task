package com.aslmmovic.tglabtask.domain.usecase


import com.aslmmovic.tglabtask.domain.model.Player
import com.aslmmovic.tglabtask.domain.repository.NbaRepository
import com.aslmmovic.tglabtask.domain.util.AppResult
import javax.inject.Inject

class SearchPlayersUseCase @Inject constructor(
    private val repo: NbaRepository
) {
    suspend operator fun invoke(query: String): AppResult<List<Player>> {
        val q = query.trim()
        if (q.isBlank()) return AppResult.Empty
        return repo.searchPlayers(q)
    }
}
