package cz.cvut.fit.houdeda2.weather_app.features.weather.presentation.current_weather

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cz.cvut.fit.houdeda2.weather_app.features.weather.data.WeatherRepository
import cz.cvut.fit.houdeda2.weather_app.features.weather.domain.WeatherLocationGeo
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class CurrentWeatherViewModel(
    private val weatherRepository: WeatherRepository
) : ViewModel() {

    private val _locationStateStream = MutableStateFlow(GeoLocationState())
    val locationStateStream = _locationStateStream.asStateFlow()

    fun getGeoForLocation(location: String) {
        viewModelScope.launch {
            val locations = weatherRepository.getGeoForLocation(location)
            if (locations.isNotEmpty()) {
                _locationStateStream.value = GeoLocationState(locations)
            } else {
                _locationStateStream.value = GeoLocationState(null)
            }
        }
    }
}

data class GeoLocationState(
    val location: List<WeatherLocationGeo>? = null,
)