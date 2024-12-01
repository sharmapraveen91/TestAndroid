package com.praveen.weathertest.data.api

import com.praveen.weathertest.data.model.WeatherResponse
import retrofit2.http.GET
import retrofit2.http.Query


/**
 * Created by Praveen.Sharma on 01/12/24
 *
 ***/
interface WeatherApi {
    @GET("current.json")
    suspend fun getCurrentWeather(
        @Query("key") apiKey: String,
        @Query("q") location: String
    ): WeatherResponse
}