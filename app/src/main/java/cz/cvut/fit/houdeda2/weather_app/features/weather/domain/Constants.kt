package cz.cvut.fit.houdeda2.weather_app.features.weather.domain

object Constants {
    fun getWeatherIconUrl(icon: String): String {
        return "https://openweathermap.org/img/wn/$icon@4x.png"
    }
}