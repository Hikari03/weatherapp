package cz.cvut.fit.houdeda2.weather_app.features.weather.data.api

import android.util.Log
import cz.cvut.fit.houdeda2.weather_app.core.data.api.ApiClient
import cz.cvut.fit.houdeda2.weather_app.core.data.datastore.DataStore
import cz.cvut.fit.houdeda2.weather_app.features.weather.domain.WeatherLocationGeo
import io.ktor.client.request.parameter
import io.ktor.http.HttpMethod
import kotlinx.coroutines.flow.first

class WeatherRemoteDataSource(
    private val apiClient: ApiClient
) {
    suspend fun getGeoLocation(locationName: String): List<WeatherLocationGeo> {

        val apiKey = DataStore.getAPIKey().first()

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

    suspend fun getLocationFromGeo(lat: Double, lon: Double): WeatherLocationGeo {
        val apiKey = DataStore.getAPIKey().first()

        Log.d("WeatherRemoteDataSource", "Fetching location for lat: $lat, lon: $lon with API")

        val responseList = apiClient.request<List<ApiWeatherGeo>>(
            endpoint = "geo/1.0/reverse",
            method = HttpMethod.Get,
        ) {
            parameter("lat", lat)
            parameter("lon", lon)
            parameter("limit", 1)
            parameter("appid", apiKey)
        }

        if (responseList.isEmpty()) {
            throw Exception("No location found for the provided coordinates")
        }

        val response = responseList.first()

        return WeatherLocationGeo(
            locationName = response.name,
            country = response.country,
            lon = response.lon,
            lat = response.lat
        )
    }

    suspend fun getWeatherByGeo(lat: Double, lon: Double): ApiWeatherData {
        val apiKey = DataStore.getAPIKey().first()

        Log.d("WeatherRemoteDataSource", "Fetching weather for lat: $lat, lon: $lon with API")

        return apiClient.request<ApiWeatherData>(
            endpoint = "/data/3.0/onecall",
            method = HttpMethod.Get,
        ) {
            parameter("lat", lat)
            parameter("lon", lon)
            parameter("units", "metric")
            parameter("appid", apiKey)
        }
    }
}