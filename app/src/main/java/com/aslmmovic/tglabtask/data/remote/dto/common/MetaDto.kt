package com.aslmmovic.tglabtask.data.remote.dto.common

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class MetaDto(
    @SerialName("per_page") val perPage: Int? = null,
    @SerialName("next_cursor") val nextCursor: Int? = null,
    @SerialName("prev_cursor") val prevCursor: Int? = null
)
