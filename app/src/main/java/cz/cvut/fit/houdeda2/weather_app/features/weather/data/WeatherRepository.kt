package cz.cvut.fit.houdeda2.weather_app.features.weather.data

import android.util.Log
import cz.cvut.fit.houdeda2.weather_app.core.data.datastore.DataStore
import cz.cvut.fit.houdeda2.weather_app.features.weather.data.api.WeatherRemoteDataSource
import cz.cvut.fit.houdeda2.weather_app.features.weather.data.db.WeatherLocalDataSource
import cz.cvut.fit.houdeda2.weather_app.features.weather.domain.Constants
import cz.cvut.fit.houdeda2.weather_app.features.weather.domain.WeatherData
import cz.cvut.fit.houdeda2.weather_app.features.weather.domain.WeatherLocationGeo
import kotlinx.coroutines.flow.first
import java.util.Date

class WeatherRepository(
    private val weatherRemoteDataSource: WeatherRemoteDataSource,
    private val weatherLocalDataSource: WeatherLocalDataSource
) {

    suspend fun getGeoForLocation(location: String): List<WeatherLocationGeo> {
        return weatherRemoteDataSource.getGeoLocation(location)
    }

    suspend fun getWeatherForSelectedLocation(): WeatherData {
        val currentLocation = DataStore.getCurrentLocationName().first()
        Log.d("WeatherRepository", "getWeatherForSelectedLocation: Current location: ${currentLocation.locationName}, ${currentLocation.country}, ${currentLocation.lat}, ${currentLocation.lon}")

        val currDate = Date()

        val cachedWeather = weatherLocalDataSource.getLatestWeather(
            locationId = weatherLocalDataSource.getLocationId(
                locationName = currentLocation.locationName,
                country = currentLocation.country,
                lat = currentLocation.lat.toString(),
                lon = currentLocation.lon.toString()
            )
        )

        if (cachedWeather != null && cachedWeather.now.time.time > currDate.time - Constants.CACHE_EXPIRATION_TIME) {
            Log.d("WeatherRepository", "getWeatherForSelectedLocation: Returning cached weather data")
            return cachedWeather
        }

        Log.d("WeatherRepository", "getWeatherForSelectedLocation: Fetching new weather data")

        try {
            val newData = getWeatherByGeo(
                locationName = currentLocation.locationName,
                country = currentLocation.country,
                lat = currentLocation.lat,
                lon = currentLocation.lon
            )
            weatherLocalDataSource.insertWeatherData(
                weatherData = newData,
                lat = currentLocation.lat.toString(),
                lon = currentLocation.lon.toString()
            )
            return newData
        } catch (e: Exception) {
            Log.e("WeatherRepository", "Error fetching weather data", e)
            // If fetching new data fails, return cached data if available
            cachedWeather?.let {
                Log.d("WeatherRepository", "Returning cached weather data due to error: ${e.message}")
                return it
            } ?: throw e // If no cached data, rethrow the exception
        }
    }

    suspend fun getWeatherByGeo(locationName: String, country: String, lat: Double, lon: Double): WeatherData {
        val apiWeatherData = weatherRemoteDataSource.getWeatherByGeo(lat = lat, lon = lon)

        val weatherNow = WeatherData.WeatherNow(
            time = Date(apiWeatherData.current.dt * 1_000),
            locationName = locationName,
            country = country,
            sunrise = Date(apiWeatherData.current.sunrise * 1_000),
            sunset = Date(apiWeatherData.current.sunset * 1_000),
            temperature = apiWeatherData.current.temp,
            feelsLike = apiWeatherData.current.feels_like,
            pressure = apiWeatherData.current.pressure,
            humidity = apiWeatherData.current.humidity,
            uvIndex = apiWeatherData.current.uvi,
            windSpeed = apiWeatherData.current.wind_speed,
            windDirection = apiWeatherData.current.wind_deg,
            description = apiWeatherData.current.weather[0].description,
            iconUrl = Constants.getWeatherIconUrl(apiWeatherData.current.weather[0].icon)
        )

        val forecastMinutely = apiWeatherData.minutely?.map { minutely ->
            WeatherData.WeatherForecastMinutely(
                time = Date(minutely.dt * 1_000),
                precipitation = minutely.precipitation
            )
        }

        val forecastHourly = apiWeatherData.hourly?.map { hourly ->
            WeatherData.WeatherForecastHourly(
                time = Date(hourly.dt * 1_000),
                temperature = hourly.temp,
                feelsLike = hourly.feels_like,
                pressure = hourly.pressure,
                humidity = hourly.humidity,
                uvIndex = hourly.uvi,
                windSpeed = hourly.wind_speed,
                windDirection = hourly.wind_deg,
                description = hourly.weather[0].description,
                iconUrl = Constants.getWeatherIconUrl(hourly.weather[0].icon),
                probabilityOfPrecipitation = hourly.pop,
                rainPrecipitation = hourly.rain?.amount ?: 0.0,
                snowPrecipitation = hourly.snow?.amount ?: 0.0
            )
        }

        val forecastDaily = apiWeatherData.daily?.map { daily ->
            WeatherData.WeatherForecastDaily(
                date = Date(daily.dt * 1_000),
                sunrise = Date(daily.sunrise * 1_000),
                sunset = Date(daily.sunset * 1_000),
                summary = daily.weather[0].description,
                temperatureDay = daily.temp.day,
                temperatureMin = daily.temp.min,
                temperatureMax = daily.temp.max,
                temperatureNight = daily.temp.night,
                temperatureEve = daily.temp.eve,
                temperatureMorn = daily.temp.morn,
                feelsLikeDay = daily.feels_like.day,
                feelsLikeNight = daily.feels_like.night,
                feelsLikeEve = daily.feels_like.eve,
                feelsLikeMorn = daily.feels_like.morn,
                pressure = daily.pressure.toDouble(),
                humidity = daily.humidity,
                windSpeed = daily.wind_speed,
                windDirection = daily.wind_deg,
                uvIndex = daily.uvi,
                probabilityOfPrecipitation = daily.pop,
                rainPrecipitation = daily.rain ?: 0.0,
                snowPrecipitation = daily.snow ?: 0.0,
                iconUrl = Constants.getWeatherIconUrl(daily.weather[0].icon),
                description = daily.weather[0].description
            )
        }

        return WeatherData(
            now = weatherNow,
            forecastMinutely = forecastMinutely,
            forecastHourly = forecastHourly,
            forecastDaily = forecastDaily
        )
    }

}