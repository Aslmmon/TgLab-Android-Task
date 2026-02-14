package com.aslmmovic.tglabtask.presentation.viewmodel


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.aslmmovic.tglabtask.domain.model.Game
import com.aslmmovic.tglabtask.domain.repository.NbaRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@HiltViewModel
class TeamGamesViewModel @Inject constructor(
    private val repo: NbaRepository
) : ViewModel() {

    fun games(teamId: Int): Flow<PagingData<Game>> {
        return repo.getTeamGames(teamId).cachedIn(viewModelScope)
    }
}
