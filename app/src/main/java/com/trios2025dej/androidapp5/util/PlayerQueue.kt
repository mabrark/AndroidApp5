package com.trios2025dej.androidapp5.util

import com.trios2025dej.androidapp5.models.PodcastResult

object PlayerQueue {
    @Volatile
    var nowPlaying: PodcastResult? = null
}
