package cz.cvut.fit.houdeda2.weather_app.features.settings.presentation

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cz.cvut.fit.houdeda2.weather_app.core.data.datastore.DataStore
import cz.cvut.fit.houdeda2.weather_app.features.weather.data.WeatherRepository
import cz.cvut.fit.houdeda2.weather_app.features.weather.domain.WeatherLocationGeo
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking


class SettingsViewModel(
    private val weatherRepository: WeatherRepository
) : ViewModel() {

    private val _locationStateStream = MutableStateFlow(LocationState())
    val locationStateStream = _locationStateStream.asStateFlow()


    fun getGeoForLocation(location: String) {
        viewModelScope.launch {
            try {
                val locations = weatherRepository.getGeoForLocation(location)
                if (locations.isNotEmpty()) {
                    _locationStateStream.value = LocationState(locations)
                } else {
                    _locationStateStream.value = LocationState(null)
                }
            } catch (e: Exception) {
                Log.e("SettingsViewModel", "Error fetching locations", e)
                _locationStateStream.value = LocationState(null, "Error fetching locations")
            }

        }
    }


    fun getAPIKey(): String {
        return runBlocking {
            Log.d("SettingsViewModel", "Getting API Key")
            DataStore.getAPIKey().first()
        }
    }

    fun setAPIKey(apiKey: String) {
        viewModelScope.launch {
            DataStore.setAPIKey(apiKey)
            Log.d("SettingsViewModel", "API Key set $apiKey")
        }
    }

    fun getCurrentLocation(): WeatherLocationGeo {
        return runBlocking {
            Log.d("SettingsViewModel", "Getting current location")
            DataStore.getCurrentLocationName().first()
        }
    }

    fun selectLocation(location: WeatherLocationGeo) {
        viewModelScope.launch {
            DataStore.setCurrentLocation(location)
        }
    }

}

data class LocationState(
    val locations: List<WeatherLocationGeo>? = null,
    val error: String? = null
)
