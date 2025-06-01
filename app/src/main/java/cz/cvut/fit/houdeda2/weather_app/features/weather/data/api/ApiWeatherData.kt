package cz.cvut.fit.houdeda2.weather_app.features.weather.data.api

import cz.cvut.fit.houdeda2.weather_app.features.weather.data.api.ApiWeatherData.ApiWeatherNow.ApiWeatherDescription
import kotlinx.serialization.Serializable

@Serializable
class ApiWeatherData(
    val timezone_offset: Int,
    val current: ApiWeatherNow,
    val minutely: List<ApiWeatherMinutely>? = null,
    val hourly: List<ApiWeatherHourly>? = null,
    val daily: List<ApiWeatherDaily>? = null
) {

    @Serializable
    class ApiWeatherNow(
        val dt: Long,
        val sunrise: Long,
        val sunset: Long,
        val temp: Double,
        val feels_like: Double,
        val pressure: Double,
        val humidity: Int,
        val uvi: Double,
        val wind_speed: Double,
        val wind_deg: Int,
        val weather: List<ApiWeatherDescription>
    ) {
        @Serializable
        class ApiWeatherDescription(
            val description: String,
            val icon: String
        )
    }

    @Serializable
    class ApiWeatherMinutely(
        val dt: Long,
        val precipitation: Double
    )

    @Serializable
    class ApiWeatherHourly(
        val dt: Long,
        val temp: Double,
        val feels_like: Double,
        val pressure: Double,
        val humidity: Int,
        val uvi: Double,
        val wind_speed: Double,
        val wind_deg: Int,
        val pop: Double, // Probability of precipitation
        val rain: ApiWeatherPrecipitation? = null, // Rain precipitation
        val snow: ApiWeatherPrecipitation? = null, // Snow precipitation
        val weather: List<ApiWeatherDescription>,
    ) {
        @Serializable
        class ApiWeatherPrecipitation(
            val amount: Double = 0.0
        )
    }

    @Serializable
    class ApiWeatherDaily(
        val dt: Long,
        val sunrise: Long,
        val sunset: Long,
        val temp: ApiWeatherTemperature,
        val feels_like: ApiWeatherFeelsLike,
        val pressure: Double,
        val humidity: Int,
        val wind_speed: Double,
        val wind_deg: Int,
        val uvi: Double,
        val pop: Double, // Probability of precipitation
        val rain: Double? = null, // Rain precipitation
        val snow: Double? = null, // Snow precipitation
        val weather: List<ApiWeatherDescription>
    ) {
        @Serializable
        class ApiWeatherTemperature(
            val morn: Double,
            val day: Double,
            val eve: Double,
            val night: Double,
            val min: Double,
            val max: Double,
        )

        @Serializable
        class ApiWeatherFeelsLike(
            val morn: Double,
            val day: Double,
            val eve: Double,
            val night: Double,
        )
    }
}