package com.trios2025dej.androidapp5.network

import com.trios2025dej.androidapp5.models.ItunesSearchResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface ItunesApi {

    @GET("search")
    suspend fun searchPodcasts(
        @Query("term") term: String,
        @Query("media") media: String = "podcast"
    ): ItunesSearchResponse
}
