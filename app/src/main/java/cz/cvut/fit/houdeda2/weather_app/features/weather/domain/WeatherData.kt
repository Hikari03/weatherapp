package cz.cvut.fit.houdeda2.weather_app.features.weather.domain


import java.util.Date

data class WeatherData(
    val now: WeatherNow,
    val forecastMinutely: List<WeatherForecastMinutely>? = null,
    val forecastHourly: List<WeatherForecastHourly>? = null,
    val forecastDaily: List<WeatherForecastDaily>? = null
) {
    data class WeatherNow(
        val time: Date,
        val locationName: String,
        val country: String,
        val sunrise: Date,
        val sunset: Date,
        val temperature: Double,
        val feelsLike: Double,
        val pressure: Double,
        val humidity: Int,
        val uvIndex: Double,
        val windSpeed: Double,
        val windDirection: Int,
        val description: String,
        val iconUrl: String
    )

    data class WeatherForecastMinutely(
        val time: Date,
        val precipitation: Double,
    )

    data class WeatherForecastHourly(
        val time: Date,
        val temperature: Double,
        val feelsLike: Double,
        val pressure: Double,
        val humidity: Int,
        val uvIndex: Double,
        val windSpeed: Double,
        val windDirection: Int,
        val description: String,
        val iconUrl: String,
        val probabilityOfPrecipitation: Double,
        val rainPrecipitation: Double = 0.0,
        val snowPrecipitation: Double = 0.0
    )

    data class WeatherForecastDaily(
        val date: Date,
        val sunrise: Date,
        val sunset: Date,
        val summary: String,
        val temperatureDay: Double,
        val temperatureMin: Double,
        val temperatureMax: Double,
        val temperatureNight: Double,
        val temperatureEve: Double,
        val temperatureMorn: Double,
        val feelsLikeDay: Double,
        val feelsLikeNight: Double,
        val feelsLikeEve: Double,
        val feelsLikeMorn: Double,
        val pressure: Double,
        val humidity: Int,
        val windSpeed: Double,
        val windDirection: Int,
        val description: String,
        val iconUrl: String,
        val probabilityOfPrecipitation: Double,
        val rainPrecipitation: Double = 0.0,
        val snowPrecipitation: Double = 0.0,
        val uvIndex: Double

    )
}