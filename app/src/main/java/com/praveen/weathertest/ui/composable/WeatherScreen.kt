package com.praveen.weathertest.ui.composable

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.praveen.weathertest.data.model.WeatherResponse


/**
 * Created by Praveen.Sharma on 01/12/24
 *
 ***/
@Composable
fun WeatherScreen(weatherResponse: WeatherResponse?) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        if (weatherResponse == null) {
            CircularProgressIndicator()
        } else {
            WeatherContent(weatherResponse)
        }
    }
}

@Composable
fun WeatherContent(weatherResponse: WeatherResponse) {
    val weather = weatherResponse.current
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // Temperature Display
        Text(
            text = "Current Temperature",
            style = MaterialTheme.typography.titleMedium.copy(
                fontWeight = FontWeight.Bold
            )
        )

        Text(
            text = "${weather.temp_c}°C",
            style = MaterialTheme.typography.headlineLarge.copy(
                color = MaterialTheme.colorScheme.primary
            ),
            modifier = Modifier.padding(top = 8.dp)
        )

        // Weather Condition
        weather.condition?.text?.let {
            Text(
                text = it,
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onBackground
            )
        }

        // Decorative Icon (optional)
        weather.condition?.icon?.let { iconUrl ->
            AsyncImage(
                model = "https:$iconUrl",
                contentDescription = "Weather Icon",
                modifier = Modifier
                    .size(64.dp)
                    .padding(top = 16.dp)
            )
        }
    }
}
