package cz.cvut.fit.houdeda2.weather_app.features.weather.presentation.current_weather

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import cz.cvut.fit.houdeda2.weather_app.core.data.datastore.SettingsDataStore
import cz.cvut.fit.houdeda2.weather_app.features.settings.presentation.LocationState
import cz.cvut.fit.houdeda2.weather_app.features.weather.data.WeatherRepository
import cz.cvut.fit.houdeda2.weather_app.features.weather.domain.WeatherData
import cz.cvut.fit.houdeda2.weather_app.features.weather.domain.WeatherLocationGeo
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class CurrentWeatherViewModel(
    private val weatherRepository: WeatherRepository
) : ViewModel() {

    private val _weatherStateStream = MutableStateFlow(WeatherState())
    val weatherStateStream = _weatherStateStream.asStateFlow()

    fun getWeather() {
        Log.d("WeatherViewModel", "getWeather called")
        viewModelScope.launch {
            _weatherStateStream.value = WeatherState(weatherRepository.getWeatherForSelectedLocation())
        }
    }

}

data class WeatherState(
    val weatherData: WeatherData? = null,
)