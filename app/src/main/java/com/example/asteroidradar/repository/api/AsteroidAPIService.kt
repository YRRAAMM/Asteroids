package com.example.asteroidradar.repository.api

import com.example.asteroidradar.repository.database.PictureOfDay
import retrofit2.http.GET

interface AsteroidAPIService {
    @GET("neo/rest/v1/feed?api_key=dZNonfABWLnqnJg8bazIRLQKLemyCfKQ2MJ3ZhqQ")
    suspend fun callAsteroids(): String

    @GET("planetary/apod?api_key=dZNonfABWLnqnJg8bazIRLQKLemyCfKQ2MJ3ZhqQ")
    suspend fun callPictureOfDay(): PictureOfDay
}