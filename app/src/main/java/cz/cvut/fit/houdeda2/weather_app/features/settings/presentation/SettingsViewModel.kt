package cz.cvut.fit.houdeda2.weather_app.features.settings.presentation

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cz.cvut.fit.houdeda2.weather_app.features.settings.data.SettingsDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking


class SettingsViewModel : ViewModel() {

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

}
