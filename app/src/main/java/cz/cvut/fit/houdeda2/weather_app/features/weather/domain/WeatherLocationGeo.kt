package cz.cvut.fit.houdeda2.weather_app.features.weather.domain

import androidx.compose.runtime.saveable.Saver

data class WeatherLocationGeo(
    val locationName: String,
    val country: String,
    val lat: Double,
    val lon: Double
)

val weatherLocationGeoSaver = Saver<WeatherLocationGeo, List<Any?>>(

    save = { location ->
        listOf(location.locationName, location.country, location.lon, location.lat)
    },
    restore = { saved ->
        WeatherLocationGeo(
            locationName = saved[0] as String,
            country = saved[1] as String,
            lon = saved[2] as Double,
            lat = saved[3] as Double
        )
    }
)