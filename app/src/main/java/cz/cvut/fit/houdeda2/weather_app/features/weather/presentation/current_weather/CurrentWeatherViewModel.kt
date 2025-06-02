package cz.cvut.fit.houdeda2.weather_app.features.weather.presentation.current_weather

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cz.cvut.fit.houdeda2.weather_app.features.weather.data.WeatherRepository
import cz.cvut.fit.houdeda2.weather_app.features.weather.domain.WeatherData
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class CurrentWeatherViewModel(
    private val weatherRepository: WeatherRepository
) : ViewModel() {

    private val _weatherStateStream = MutableStateFlow(WeatherState())
    val weatherStateStream = _weatherStateStream.asStateFlow()

    fun getWeather() {
        viewModelScope.launch {
            try {
                _weatherStateStream.value = WeatherState(weatherRepository.getWeatherForSelectedLocation())
            } catch (e: Exception) {
                Log.e("WeatherViewModel", "Error fetching weather data", e)
                _weatherStateStream.value = WeatherState(weatherData = null, message = "Error fetching data" )
            }
        }
    }

}

data class WeatherState(
    val weatherData: WeatherData? = null,
    val message: String? = null
)