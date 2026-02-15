package com.aslmmovic.tglabtask.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aslmmovic.tglabtask.domain.model.Player
import com.aslmmovic.tglabtask.domain.usecase.SearchPlayersUseCase
import com.aslmmovic.tglabtask.presentation.util.UiState
import com.aslmmovic.tglabtask.presentation.util.toUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.drop
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PlayersViewModel @Inject constructor(
    private val searchPlayers: SearchPlayersUseCase
) : ViewModel() {

    private val _query = MutableStateFlow("")
    val query: StateFlow<String> = _query.asStateFlow()

    private val _state = MutableStateFlow<UiState<List<Player>>>(UiState.Empty)
    val state: StateFlow<UiState<List<Player>>> = _state.asStateFlow()

    init {
        observeQuery()
    }

    fun onQueryChange(value: String) {
        _query.value = value
        if (value.isBlank()) {
            _state.value = UiState.Empty
        }
    }

    fun retry() {
        performSearch(_query.value)
    }

    @OptIn(FlowPreview::class)
    private fun observeQuery() {
        viewModelScope.launch {
            _query
                .drop(1)
                .debounce(400)
                .distinctUntilChanged()
                .collect { q ->
                    performSearch(q)
                }
        }
    }

    private fun performSearch(q: String) {
        val trimmed = q.trim()
        if (trimmed.isBlank()) return

        _state.value = UiState.Loading
        viewModelScope.launch {
            val result = searchPlayers(trimmed)
            _state.value = result.toUiState()
        }
    }
}
