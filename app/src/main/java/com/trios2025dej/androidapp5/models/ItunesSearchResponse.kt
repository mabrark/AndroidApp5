package com.trios2025dej.androidapp5.models

data class ItunesSearchResponse(
    val resultCount: Int = 0,
    val results: List<PodcastResult>? = emptyList()
)
