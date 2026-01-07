package com.trios2025dej.androidapp5.models

data class ItunesPodcast(
    val collectionId: Long,
    val collectionName: String,
    val artistName: String?,
    val artworkUrl100: String?,
    val feedUrl: String?,      // iTunes usually gives feed URL
    val audioUrl: String? = null  // optional if you’re playing a sample/episode URL
)
