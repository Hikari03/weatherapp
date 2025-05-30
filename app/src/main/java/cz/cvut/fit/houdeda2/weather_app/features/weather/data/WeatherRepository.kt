package cz.cvut.fit.houdeda2.weather_app.features.weather.data

import cz.cvut.fit.houdeda2.weather_app.features.weather.data.api.WeatherRemoteDataSource
import cz.cvut.fit.houdeda2.weather_app.features.weather.domain.WeatherLocationGeo

class WeatherRepository(
    private val weatherRemoteDataSource: WeatherRemoteDataSource
) {

    suspend fun getGeoForLocation(location: String): List<WeatherLocationGeo> {
        return weatherRemoteDataSource.getGeoLocation(location)
    }

}