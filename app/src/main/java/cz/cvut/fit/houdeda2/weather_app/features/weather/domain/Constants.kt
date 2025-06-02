package cz.cvut.fit.houdeda2.weather_app.features.weather.domain

object Constants {
    fun getWeatherIconUrl(icon: String): String {
        return "https://openweathermap.org/img/wn/$icon@4x.png"
    }

    const val CACHE_EXPIRATION_TIME = 60 * 10 * 1000 // 10 minutes
}