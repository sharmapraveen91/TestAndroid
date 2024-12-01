package com.praveen.weathertest.data.repository

import com.praveen.weathertest.data.api.WeatherApi
import com.praveen.weathertest.data.model.WeatherResponse
import com.praveen.weathertest.domain.repository.WeatherRepository
import javax.inject.Inject


/**
 * Created by Praveen.Sharma on 01/12/24
 *
 ***/
class WeatherRepositoryImpl @Inject constructor(private val api: WeatherApi) :
    WeatherRepository {
    override suspend fun getCurrentWeather(apiKey: String, location: String): WeatherResponse {
        return api.getCurrentWeather(apiKey = apiKey, location = location)
    }
}