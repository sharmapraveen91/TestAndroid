package com.praveen.weathertest.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.praveen.weathertest.data.model.WeatherResponse
import com.praveen.weathertest.domain.repository.WeatherRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject


/**
 * Created by Praveen.Sharma on 01/12/24
 *
 ***/
@HiltViewModel
class WeatherViewModel @Inject constructor(
    private val repository: WeatherRepository,
) : ViewModel() {

    private val _weather = MutableStateFlow<WeatherResponse?>(null)
    val weather: StateFlow<WeatherResponse?> = _weather

    private val _cityName = MutableStateFlow<String?>(null)
    val cityName: StateFlow<String?> = _cityName

    fun fetchWeather(apiKey: String, cityName: String?) {
        viewModelScope.launch {
            cityName?.let { city ->
                try {
                    val response = repository.getCurrentWeather(apiKey, city)
                    _weather.value = response
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
    }

    fun setCityName(cityName: String?) {
        var updatedCity = cityName
        if (cityName == "Gurugram") { // geocoder gives cityName as Gurugram but weatherApi don't return result for Gurugram
            updatedCity = "Gurgaon"
        }
        _cityName.update { updatedCity }

    }
}