package com.aslmmovic.tglabtask.domain.model

enum class TeamSort { NAME, CITY, CONFERENCE }

fun TeamSort.toButtonTitle(): String = when (this) {
    TeamSort.NAME -> "Name"
    TeamSort.CITY -> "City"
    TeamSort.CONFERENCE -> "Conference"
}

