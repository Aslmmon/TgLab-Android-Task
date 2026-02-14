package com.aslmmovic.tglabtask.presentation.viewmodel


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.aslmmovic.tglabtask.domain.repository.NbaRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flatMapLatest
import javax.inject.Inject

@HiltViewModel
class TeamGamesViewModel @Inject constructor(
    private val repo: NbaRepository
) : ViewModel() {

    private val teamId = MutableStateFlow<Int?>(null)
    @OptIn(ExperimentalCoroutinesApi::class)
    val gamesFlow = teamId
        .filterNotNull()
        .distinctUntilChanged()
        .flatMapLatest { id -> repo.getTeamGames(id) }
        .cachedIn(viewModelScope)

    fun setTeamId(id: Int) {
        teamId.value = id
    }

}
