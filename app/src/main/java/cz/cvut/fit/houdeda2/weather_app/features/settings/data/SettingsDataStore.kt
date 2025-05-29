package cz.cvut.fit.houdeda2.weather_app.features.settings.data

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import cz.cvut.fit.houdeda2.weather_app.appContext
import kotlinx.coroutines.flow.first

object SettingsDataStore {

    private val apiStoreKey = stringPreferencesKey("api_key")
    private val Context.dataStore by preferencesDataStore(name = "settings")

    suspend fun getAPIKey(): String {
        val preferences = appContext.dataStore.data.first()
        return preferences[apiStoreKey] ?: ""
    }

    suspend fun setAPIKey(apiKey: String) {
        appContext.dataStore.edit { preferences ->
            preferences[apiStoreKey] = apiKey
        }
    }

}