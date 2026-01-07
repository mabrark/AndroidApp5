package com.trios2025dej.androidapp5.models

import com.google.gson.annotations.SerializedName

data class ItunesSearchResponse(
    @SerializedName("resultCount") val resultCount: Int,
    @SerializedName("results") val results: List<PodcastResult>
)

data class PodcastResult(
    @SerializedName("collectionId") val collectionId: Long,
    @SerializedName("collectionName") val collectionName: String?,
    @SerializedName("artistName") val artistName: String?,
    @SerializedName("artworkUrl100") val artworkUrl100: String?,
    @SerializedName("feedUrl") val feedUrl: String?,
    @SerializedName("previewUrl") val previewUrl: String? // sometimes present
)

/**
 * What the Player plays.
 * For now we use previewUrl if present.
 * Later you’ll parse feedUrl RSS into real episodes.
 */
data class Episode(
    val title: String,
    val audioUrl: String
)
