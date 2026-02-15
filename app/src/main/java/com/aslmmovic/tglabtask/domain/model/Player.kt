package com.aslmmovic.tglabtask.domain.model

data class Player(
    val id: Int,
    val firstName: String,
    val lastName: String,
    val teamId: Int?,        // can be null
    val teamName: String ,
    val teamData: Team?    // “Unknown” if missing
// “Unknown” if missing
)
