package com.aslmmovic.tglabtask.domain.model

data class Player(
    val id: Int,
    val firstName: String,
    val lastName: String,
    val teamId: Int?,        // can be null
    val teamName: String     // “Unknown” if missing
)
