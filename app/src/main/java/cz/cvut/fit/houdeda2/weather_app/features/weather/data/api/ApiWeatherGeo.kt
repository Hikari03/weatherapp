package cz.cvut.fit.houdeda2.weather_app.features.weather.data.api

import kotlinx.serialization.Serializable

@Serializable
class ApiWeatherGeo(
    val name: String,
    val lat: Double,
    val lon: Double,
    val country: String,
)