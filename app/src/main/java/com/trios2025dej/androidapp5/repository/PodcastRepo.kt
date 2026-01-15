package com.trios2025dej.androidapp5.repository

import com.trios2025dej.androidapp5.models.Episode
import com.trios2025dej.androidapp5.service.RssFeedService

class PodcastRepo(
    private val rssService: RssFeedService = RssFeedService.instance
) {
    suspend fun fetchEpisodes(feedUrl: String): List<Episode> {
        // Your RssFeedService returns RssFeedResponse which contains EpisodeResponse (strings)
        val rss = rssService.getFeed(feedUrl) ?: return emptyList()

        // Convert EpisodeResponse -> Episode model
        val list = rss.episodes.orEmpty().map { ep ->
            Episode(
                title = ep.title,
                description = ep.description,
                audioUrl = ep.url,
                durationMillis = com.trios2025dej.androidapp5.util.DateUtils.durationTextToMillis(ep.duration),
                releaseDate = com.trios2025dej.androidapp5.util.DateUtils.xmlDateToDate(ep.pubDate)
            )
        }

        return list
    }
}
