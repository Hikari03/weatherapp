package cz.cvut.fit.houdeda2.weather_app.features.weather.presentation.current_weather

import androidx.lifecycle.ViewModel
import cz.cvut.fit.houdeda2.weather_app.core.data.datastore.SettingsDataStore
import cz.cvut.fit.houdeda2.weather_app.features.weather.data.WeatherRepository
import cz.cvut.fit.houdeda2.weather_app.features.weather.domain.WeatherLocationGeo
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking

class CurrentWeatherViewModel(
    private val weatherRepository: WeatherRepository
) : ViewModel() {


    fun getCurrentLocation(): WeatherLocationGeo {
        return runBlocking {
            SettingsDataStore.getCurrentLocationName().first()
        }
    }

}