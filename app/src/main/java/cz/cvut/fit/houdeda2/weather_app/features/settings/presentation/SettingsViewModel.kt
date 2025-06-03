package cz.cvut.fit.houdeda2.weather_app.features.settings.presentation

import android.util.Log
import androidx.annotation.RequiresPermission
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.logEvent
import cz.cvut.fit.houdeda2.weather_app.core.data.LocationProvider
import cz.cvut.fit.houdeda2.weather_app.core.data.datastore.DataStore
import cz.cvut.fit.houdeda2.weather_app.features.weather.data.WeatherRepository
import cz.cvut.fit.houdeda2.weather_app.features.weather.domain.WeatherLocationGeo
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking


class SettingsViewModel(
    private val weatherRepository: WeatherRepository,
    private val locationProvider: LocationProvider,
    private val firebaseAnalytics: FirebaseAnalytics
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

            firebaseAnalytics.logEvent("get_geo_for_location") {
                param("location", location)
                if (_locationStateStream.value.locations != null) {
                    param("locations_count", _locationStateStream.value.locations!!.size.toLong())
                } else {
                    param("error_message", _locationStateStream.value.error ?: "No locations found")
                }
            }

        }
    }

    @RequiresPermission(anyOf = [android.Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.ACCESS_COARSE_LOCATION])
    fun getUserLocation() {
        viewModelScope.launch {
            try {
                val location = locationProvider.getLocation()

                if (location != null) {
                    val weatherLocation = weatherRepository.getLocationFromGeo(
                        lat = location.latitude,
                        lon = location.longitude,
                    )

                    Log.d("SettingsViewModel", "Current location: $weatherLocation")

                    _locationStateStream.value = LocationState(
                        listOf(weatherLocation),
                        "Location retrieved successfully"
                    )

                    selectLocation(weatherLocation)

                    firebaseAnalytics.logEvent("get_user_location") {
                        param("action", "fetch_user_location")
                        param("result", "success")
                    }

                } else {
                    Log.w("SettingsViewModel", "No last known location available")
                    _locationStateStream.value = LocationState(
                        null,
                        "Could not retrieve last known location. Please enable location services."
                    )
                    firebaseAnalytics.logEvent("get_user_location") {
                        param("action", "fetch_user_location")
                        param("result", "no_location")
                        param("error_message", "No last known location available")
                    }
                }
            } catch (e: Exception) {
                Log.e("SettingsViewModel", "Error getting last location", e)
                _locationStateStream.value = LocationState(
                    null,
                    "Could not retrieve last known location. Please enable location services."
                )
                firebaseAnalytics.logEvent("get_user_location") {
                    param("action", "fetch_user_location")
                    param("result", "error")
                    param("error_message", e.message ?: "Unknown error")
                }
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

            firebaseAnalytics.logEvent("set_api_key"){

            }
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

        firebaseAnalytics.logEvent("select_location") {
            param("location_name", location.locationName)
            param("country", location.country)
        }
    }

}

data class LocationState(
    val locations: List<WeatherLocationGeo>? = null,
    val error: String? = null
)
