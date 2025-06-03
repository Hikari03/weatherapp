package cz.cvut.fit.houdeda2.weather_app.core.data.datastore

import android.content.Context
import android.util.Log
import androidx.datastore.preferences.core.doublePreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import cz.cvut.fit.houdeda2.weather_app.appContext
import cz.cvut.fit.houdeda2.weather_app.features.weather.domain.WeatherLocationGeo
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

object DataStore {

    private val apiStoreKey = stringPreferencesKey("api_key")
    private val currLocation_nameKey = stringPreferencesKey("current_location_name")
    private val currLocation_countryKey = stringPreferencesKey("current_location_country")
    private val currLocation_latKey = doublePreferencesKey("current_location_lat")
    private val currLocation_lonKey = doublePreferencesKey("current_location_lon")
    private val Context.settingsDataStore by preferencesDataStore(name = "settings")

    fun getAPIKey(): Flow<String> {
        return appContext.settingsDataStore.data.map { preferences ->
            preferences[apiStoreKey] ?: ""
        }
    }

    suspend fun setAPIKey(apiKey: String) {
        appContext.settingsDataStore.edit { preferences ->
            preferences[apiStoreKey] = apiKey
        }
    }

    fun getCurrentLocationName(): Flow<WeatherLocationGeo> {
        return appContext.settingsDataStore.data.map { preferences ->
            val name = preferences[currLocation_nameKey] ?: ""
            val country = preferences[currLocation_countryKey] ?: ""
            val lat = preferences[currLocation_latKey] ?: 0.0
            val lon = preferences[currLocation_lonKey] ?: 0.0
            Log.d("SettingsDataStore", "get: Current location: $name, $country, $lat, $lon")
            WeatherLocationGeo(name, country, lat, lon)
        }
    }

    suspend fun setCurrentLocation(location: WeatherLocationGeo) {
        Log.d(
            "SettingsDataStore",
            "set: Current location: ${location.locationName}, ${location.country}, ${location.lat}, ${location.lon}"
        )
        appContext.settingsDataStore.edit { preferences ->
            preferences[currLocation_nameKey] = location.locationName
            preferences[currLocation_countryKey] = location.country
            preferences[currLocation_latKey] = location.lat
            preferences[currLocation_lonKey] = location.lon
        }
    }

}