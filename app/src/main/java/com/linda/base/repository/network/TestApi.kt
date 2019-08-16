package com.linda.base.repository.network

import com.linda.base.repository.model.musicRanking
import retrofit2.http.GET

interface TestApi {

    @GET("/musicRankings")
    suspend fun musicRanking(): musicRanking

}