package com.trios2025dej.androidapp5.models

import java.util.Date

data class Episode(
    val title: String? = null,
    val description: String? = null,
    val audioUrl: String? = null,
    val durationMillis: Long? = null,
    val releaseDate: Date? = null
)