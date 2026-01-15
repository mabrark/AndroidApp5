package com.trios2025dej.androidapp5.models

/**
 * Matches what SearchFragment / PlayerFragment / SubscriptionsManager / adapters expect.
 * (iTunes Search API naming)
 */
data class PodcastResult(
    val collectionId: Long? = null,
    val collectionName: String? = null,
    val artistName: String? = null,
    val artworkUrl100: String? = null,
    val feedUrl: String? = null,
    val previewUrl: String? = null
)
