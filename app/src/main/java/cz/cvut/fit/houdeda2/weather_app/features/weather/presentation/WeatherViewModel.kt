package cz.cvut.fit.houdeda2.weather_app.features.weather.presentation

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.logEvent
import cz.cvut.fit.houdeda2.weather_app.features.weather.data.WeatherRepository
import cz.cvut.fit.houdeda2.weather_app.features.weather.domain.WeatherData
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class CurrentWeatherViewModel(
    private val weatherRepository: WeatherRepository,
    private val firebaseAnalytics: FirebaseAnalytics
) : ViewModel() {

    private val _weatherStateStream = MutableStateFlow(WeatherState())
    val weatherStateStream = _weatherStateStream.asStateFlow()

    fun getWeather() {
        viewModelScope.launch {
            try {
                _weatherStateStream.value =
                    WeatherState(weatherRepository.getWeatherForSelectedLocation())
            } catch (e: Exception) {
                Log.e(
                    "WeatherViewModel",
                    "Error fetching weather data\nYou are not connected to the internet or don't have valid API Key",
                    e
                )

                firebaseAnalytics.logEvent("get_weather") {
                    param("action", "fetch_weather")
                    param("error_message", e.message ?: "Unknown error")
                    param("returned_error", "Error fetching weather data, You are not connected to the internet or don't have valid API Key")
                }

                _weatherStateStream.value =
                    WeatherState(weatherData = null, message = "Error fetching data")
            }
        }

        val weatherString = if (_weatherStateStream.value.weatherData != null) {
            _weatherStateStream.value.weatherData!!.now.locationName
        } else {
            "No location"
        }

        firebaseAnalytics.logEvent("get_weather"){
            param("action", "fetch_weather")
            param("location", weatherString )
        }
    }

}

data class WeatherState(
    val weatherData: WeatherData? = null,
    val message: String? = null
)