package com.praveen.weathertest

import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.core.app.ActivityCompat
import android.Manifest.permission
import android.annotation.SuppressLint
import android.location.Geocoder
import android.util.Log
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.collectAsState
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.firebase.FirebaseApp
import com.praveen.weathertest.ui.composable.WeatherScreen
import com.praveen.weathertest.ui.viewmodel.WeatherViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.IOException
import java.util.Locale
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val viewModel: WeatherViewModel by viewModels()

    @Inject
    lateinit var fusedLocationProviderClient: FusedLocationProviderClient

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        FirebaseApp.initializeApp(this)
        setContent {
            WeatherApp(viewModel)
        }
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    @Composable
    private fun WeatherApp(viewModel: WeatherViewModel) {
        val weatherState by viewModel.weather.collectAsState()
        val context = LocalContext.current

        LaunchedEffect(Unit) {
            fetchLocationAndWeather(context)
        }

        MaterialTheme {
            WeatherScreen(weatherState)
        }
    }

    @SuppressLint("MissingPermission")
    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    private fun fetchLocationAndWeather(context: Context) {
        if (!checkLocationPermissions()) {
            requestLocationPermissions()
            return
        }

        fusedLocationProviderClient.lastLocation.addOnSuccessListener { location ->
            if (location == null) {
                Log.e("MainActivity", "Location is null")
                return@addOnSuccessListener
            }

            fetchCityAndWeather(context, location.latitude, location.longitude)
        }.addOnFailureListener {
            Log.e("MainActivity", "Failed to fetch location: ${it.message}", it)
        }
    }

    private fun fetchCityAndWeather(context: Context, latitude: Double, longitude: Double) {
        val geocoder = Geocoder(context, Locale.getDefault())
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val addresses = geocoder.getFromLocation(latitude, longitude, 1)
                if (!addresses.isNullOrEmpty()) {
                    val cityName = addresses[0].locality
                    if (cityName != null) {
                        viewModel.setCityName(cityName)
                        fetchWeatherForCity(viewModel.cityName.value.orEmpty())
                    } else {
                        Log.e("MainActivity", "City name is null")
                    }
                } else {
                    Log.e("MainActivity", "No addresses found")
                }
            } catch (e: IOException) {
                Log.e("MainActivity", "Geocoding error: ${e.message}", e)
            }
        }
    }

    private fun fetchWeatherForCity(cityName: String) {
        val apiKey = fetchApiKey()
        if (apiKey.isNotBlank()) {
            viewModel.fetchWeather(apiKey, cityName)
        } else {
            Log.e("MainActivity", "API Key is blank")
        }
    }

    private fun fetchApiKey(): String {
        return try {
            BuildConfig.WEATHER_API_KEY // Placeholder for remote config logic
        } catch (e: Exception) {
            Log.e("MainActivity", "Error fetching API key: ${e.message}", e)
            ""
        }
    }

    private fun checkLocationPermissions(): Boolean {
        return ActivityCompat.checkSelfPermission(
            this, permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED || ActivityCompat.checkSelfPermission(
            this, permission.ACCESS_COARSE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
    }

    @SuppressLint("NewApi")
    private fun requestLocationPermissions() {
        locationPermissionLauncher.launch(
            arrayOf(
                permission.ACCESS_FINE_LOCATION,
                permission.ACCESS_COARSE_LOCATION
            )
        )
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    private val locationPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
            val fineLocationGranted = permissions[permission.ACCESS_FINE_LOCATION] ?: false
            val coarseLocationGranted = permissions[permission.ACCESS_COARSE_LOCATION] ?: false

            if (fineLocationGranted || coarseLocationGranted) {
                CoroutineScope(Dispatchers.Main).launch {
                    fetchLocationAndWeather(this@MainActivity)
                }
            } else {
                Toast.makeText(this, "Location permission is required", Toast.LENGTH_SHORT).show()
            }
        }
}