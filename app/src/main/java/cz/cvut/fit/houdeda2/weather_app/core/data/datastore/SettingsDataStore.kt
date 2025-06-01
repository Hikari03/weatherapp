package cz.cvut.fit.houdeda2.weather_app.core.data.datastore

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import cz.cvut.fit.houdeda2.weather_app.appContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

object SettingsDataStore {

    private val apiStoreKey = stringPreferencesKey("api_key")
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

}