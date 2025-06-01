package cz.cvut.fit.houdeda2.weather_app.features.settings.presentation

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cz.cvut.fit.houdeda2.weather_app.core.data.datastore.SettingsDataStore
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
            val locations = weatherRepository.getGeoForLocation(location)
            if (locations.isNotEmpty()) {
                _locationStateStream.value = LocationState(locations)
            } else {
                _locationStateStream.value = LocationState(null)
            }
        }
    }


    fun getAPIKey(): String {
        return runBlocking {
            Log.d("SettingsViewModel", "Getting API Key")
            SettingsDataStore.getAPIKey().first()
        }
    }

    fun setAPIKey(apiKey: String) {
        viewModelScope.launch {
            SettingsDataStore.setAPIKey(apiKey)
            Log.d("SettingsViewModel", "API Key set $apiKey")
        }
    }

    fun getCurrentLocation(): WeatherLocationGeo {
        return runBlocking {
            Log.d("SettingsViewModel", "Getting current location")
            SettingsDataStore.getCurrentLocationName().first()
        }
    }

    fun selectLocation(location: WeatherLocationGeo) {
        viewModelScope.launch {
            SettingsDataStore.setCurrentLocation(location)
        }
    }

}

data class LocationState(
    val locations: List<WeatherLocationGeo>? = null
)
