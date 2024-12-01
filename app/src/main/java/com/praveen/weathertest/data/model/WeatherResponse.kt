package com.praveen.weathertest.data.model


/**
 * Created by Praveen.Sharma on 01/12/24
 *
 ***/
data class WeatherResponse(
    val location: Location,
    val current: Current
)

data class Location(
    val name: String,
    val region: String,
    val country: String
)

data class Current(
    val temp_c: Float,
    val condition: Condition?
)

data class Condition(
    val text: String?,
    val icon: String
)