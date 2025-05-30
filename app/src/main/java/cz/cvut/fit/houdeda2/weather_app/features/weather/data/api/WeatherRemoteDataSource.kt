package cz.cvut.fit.houdeda2.weather_app.features.weather.data.api

import cz.cvut.fit.houdeda2.weather_app.core.data.api.ApiClient
import cz.cvut.fit.houdeda2.weather_app.core.data.datastore.SettingsDataStore
import cz.cvut.fit.houdeda2.weather_app.features.weather.domain.WeatherLocationGeo
import io.ktor.client.request.parameter
import io.ktor.http.HttpMethod
import kotlinx.coroutines.flow.first

class WeatherRemoteDataSource(
    private val apiClient: ApiClient
) {
    suspend fun getGeoLocation(locationName: String): List<WeatherLocationGeo> {

        val apiKey = SettingsDataStore.getAPIKey().first()

        val response = apiClient.request<List<ApiWeatherGeo>>(
            endpoint = "geo/1.0/direct",
            method = HttpMethod.Get,
        ) {
            parameter("q", locationName)
            parameter("limit", 5)
            parameter("appid", apiKey)
        }

        return response.map { apiGeo ->
            WeatherLocationGeo(
                locationName = apiGeo.name,
                country = apiGeo.country,
                lon = apiGeo.lon,
                lat = apiGeo.lat
            )
        }

    }
}