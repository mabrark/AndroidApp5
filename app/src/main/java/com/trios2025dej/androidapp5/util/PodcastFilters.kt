package com.trios2025dej.androidapp5.util

import com.trios2025dej.androidapp5.models.PodcastResult

object PodcastFilters {

    // Regex filter by collectionName
    fun filterByRegex(items: List<PodcastResult>, regex: String): List<PodcastResult> {
        val r = Regex(regex, RegexOption.IGNORE_CASE)
        return items.filter { (it.collectionName ?: "").matches(r) }
    }

    // Filter by "title word count" (advanced criteria example)
    fun filterByWordCount(items: List<PodcastResult>, minWords: Int): List<PodcastResult> {
        return items.filter {
            val words = (it.collectionName ?: "")
                .trim()
                .split(Regex("\\s+"))
                .filter { w -> w.isNotBlank() }
            words.size >= minWords
        }
    }
}
