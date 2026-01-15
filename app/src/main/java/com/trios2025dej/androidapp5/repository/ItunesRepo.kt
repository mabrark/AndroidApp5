package com.trios2025dej.androidapp5.repository

import com.trios2025dej.androidapp5.service.ItunesService
import com.trios2025dej.androidapp5.service.PodcastResponse
import retrofit2.Response

class ItunesRepo(
    private val itunesService: ItunesService
) {
    suspend fun searchByTerm(term: String): Response<PodcastResponse> =
        itunesService.searchPodcastByTerm(term)
}