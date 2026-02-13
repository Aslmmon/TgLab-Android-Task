package com.aslmmovic.tglabtask.presentation.viewmodel


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aslmmovic.tglabtask.domain.repository.NbaRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SmokeVm @Inject constructor(
    private val repo: NbaRepository
) : ViewModel() {

    fun testTeams() {
        viewModelScope.launch {
            val teams = repo.getTeams()
            println("TEAMS SIZE = ${teams.size}")
            println("FIRST TEAM = ${teams.firstOrNull()}")
        }
    }
}
