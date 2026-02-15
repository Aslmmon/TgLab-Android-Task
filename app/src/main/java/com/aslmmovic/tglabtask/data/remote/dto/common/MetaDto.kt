package com.aslmmovic.tglabtask.data.remote.dto.common

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class MetaDto(
    // v1-style pagination (older)
//    @SerialName("total_pages") val totalPages: Int? = null,
//    @SerialName("current_page") val currentPage: Int? = null,
//    @SerialName("next_page") val nextPage: Int? = null,
    @SerialName("per_page") val perPage: Int? = null,
//    @SerialName("total_count") val totalCount: Int? = null,

    // v2-style cursor pagination (OpenAPI spec)
    @SerialName("next_cursor") val nextCursor: Int? = null,
    @SerialName("prev_cursor") val prevCursor: Int? = null
)
