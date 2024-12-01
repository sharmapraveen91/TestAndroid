package com.praveen.weathertest.domain.repository


/**
 * Created by Praveen.Sharma on 01/12/24
 *
 ***/

import com.praveen.weathertest.data.model.WeatherResponse

interface WeatherRepository {
    suspend fun getCurrentWeather(apiKey: String, location: String): WeatherResponse
}