package com.aslmmovic.tglabtask.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aslmmovic.tglabtask.domain.model.Team
import com.aslmmovic.tglabtask.domain.model.TeamSort
import com.aslmmovic.tglabtask.domain.repository.NbaRepository
import com.aslmmovic.tglabtask.domain.usecase.GetTeamsUseCase
import com.aslmmovic.tglabtask.domain.util.AppResult
import com.aslmmovic.tglabtask.presentation.util.UiState
import com.aslmmovic.tglabtask.presentation.util.toUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getTeams: GetTeamsUseCase
) : ViewModel() {

    private val _sort = MutableStateFlow(TeamSort.NAME)
    val sort: StateFlow<TeamSort> = _sort.asStateFlow()

    private val _state = MutableStateFlow<UiState<List<Team>>>(UiState.Loading)
    val state: StateFlow<UiState<List<Team>>> = _state.asStateFlow()

    init {
        loadTeams()
    }

    fun onSortSelected(newSort: TeamSort) {
        if (_sort.value == newSort) return
        _sort.value = newSort
        loadTeams()
    }

    fun retry() = loadTeams()

    private fun loadTeams() {
        _state.value = UiState.Loading
        viewModelScope.launch {
            val result = getTeams(sort = _sort.value)
            _state.value = result.toUiState()
        }
    }
}

