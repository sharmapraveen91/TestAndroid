package com.praveen.weathertest

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.praveen.weathertest.data.model.Condition
import com.praveen.weathertest.data.model.Current
import com.praveen.weathertest.data.model.Location
import com.praveen.weathertest.data.model.WeatherResponse
import com.praveen.weathertest.domain.repository.WeatherRepository
import com.praveen.weathertest.ui.viewmodel.WeatherViewModel
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith


/**
 * Created by Praveen.Sharma on 01/12/24
 *
 ***/
@RunWith(AndroidJUnit4::class)
@OptIn(ExperimentalCoroutinesApi::class)
class WeatherViewModelTest {

    private lateinit var viewModel: WeatherViewModel
    private val repository: WeatherRepository = mockk()

    @Before
    fun setUp() {
        Dispatchers.setMain(StandardTestDispatcher())
        viewModel = WeatherViewModel(repository)
    }

    @Test
    fun `fetchWeather updates weather state with response`() = runTest {
        // Mock data
        val mockResponse = WeatherResponse(
            location = Location(name = "Gurgaon", region= "Haryana", country = "India"),
            current = Current(temp_c = 25.0f,  condition = Condition("Sunny", "icon_url"))
        )
        coEvery { repository.getCurrentWeather(any(), any()) } returns mockResponse

        // Action
        viewModel.fetchWeather("mock_api_key", "MockCity")
        advanceUntilIdle()

        // Assertion
        assertEquals(mockResponse, viewModel.weather.first())
        coVerify { repository.getCurrentWeather("mock_api_key", "MockCity") }
    }

    @Test
    fun `fetchWeather does not update weather state when cityName is null`() = runTest {
        // Action
        viewModel.fetchWeather("mock_api_key", null)
        advanceUntilIdle()

        // Assertion
        assertEquals(null, viewModel.weather.first())
        coVerify(exactly = 0) { repository.getCurrentWeather(any(), any()) }
    }

    @Test
    fun `setCityName updates city name correctly`() = runTest {
        // Action for regular city name
        viewModel.setCityName("New York")
        advanceUntilIdle()

        // Assertion
        assertEquals("New York", viewModel.cityName.first())

        // Action for Gurugram
        viewModel.setCityName("Gurugram")
        advanceUntilIdle()

        // Assertion for Gurgaon transformation
        assertEquals("Gurgaon", viewModel.cityName.first())
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }
}