package cz.cvut.fit.houdeda2.weather_app.features.weather.data.db

import cz.cvut.fit.houdeda2.weather_app.features.weather.domain.WeatherData
import java.util.Date

class WeatherLocalDataSource(
    private val weatherDao: WeatherDao
) {

    suspend fun getLatestWeather(locationId: String): WeatherData? {
        return weatherDao.getLatestWeather(locationId)?.toDomain()
    }


    suspend fun insertWeatherData(weatherData: WeatherData, lat: String, lon: String) {
        weatherDao.insertWeatherData(weatherData.toDb(lat = lat, lon = lon))
    }

    fun getLocationId(locationName: String, country: String, lat: String, lon: String): String {
        return "${locationName}_${country}_${lat.take(2)}_${lon.take(2)}"
    }


    /**
     * .toDomain() converts the database model to the domain model.
     */
    private fun DbWeatherData.toDomain(): WeatherData {
        return WeatherData(
            now = now.toDomain(),
            forecastMinutely = forecastMinutely?.map { it.toDomain() },
            forecastHourly = forecastHourly?.map { it.toDomain() },
            forecastDaily = forecastDaily?.map { it.toDomain() }
        )

    }

    private fun DbWeatherData.DbWeatherNow.toDomain(): WeatherData.WeatherNow {
        return WeatherData.WeatherNow(
            time = time,
            locationName = locationName,
            country = country,
            sunrise = sunrise,
            sunset = sunset,
            temperature = temperature,
            feelsLike = feelsLike,
            pressure = pressure,
            humidity = humidity,
            uvIndex = uvIndex,
            windSpeed = windSpeed,
            windDirection = windDirection,
            description = description,
            iconUrl = iconUrl
        )
    }

    private fun DbWeatherData.DbWeatherForecastMinutely.toDomain(): WeatherData.WeatherForecastMinutely {
        return WeatherData.WeatherForecastMinutely(
            time = time,
            precipitation = precipitation
        )
    }

    private fun DbWeatherData.DbWeatherForecastHourly.toDomain(): WeatherData.WeatherForecastHourly {
        return WeatherData.WeatherForecastHourly(
            time = time,
            temperature = temperature,
            feelsLike = feelsLike,
            pressure = pressure,
            humidity = humidity,
            uvIndex = uvIndex,
            windSpeed = windSpeed,
            windDirection = windDirection,
            description = description,
            iconUrl = iconUrl,
            probabilityOfPrecipitation = probabilityOfPrecipitation,
            rainPrecipitation = rainPrecipitation,
            snowPrecipitation = snowPrecipitation
        )
    }

    private fun DbWeatherData.DbWeatherForecastDaily.toDomain(): WeatherData.WeatherForecastDaily {
        return WeatherData.WeatherForecastDaily(
            date = date,
            sunrise = sunrise,
            sunset = sunset,
            summary = summary,
            temperatureDay = temperatureDay,
            temperatureMin = temperatureMin,
            temperatureMax = temperatureMax,
            temperatureNight = temperatureNight,
            temperatureEve = temperatureEve,
            temperatureMorn = temperatureMorn,
            feelsLikeDay = feelsLikeDay,
            feelsLikeNight = feelsLikeNight,
            feelsLikeEve = feelsLikeEve,
            feelsLikeMorn = feelsLikeMorn,
            pressure = pressure,
            humidity = humidity,
            windSpeed = windSpeed,
            windDirection = windDirection,
            description = description,
            iconUrl = iconUrl,
            probabilityOfPrecipitation = probabilityOfPrecipitation,
            rainPrecipitation = rainPrecipitation,
            snowPrecipitation = snowPrecipitation,
            uvIndex = uvIndex
        )
    }

    /**
     * .toDb() converts the domain model to the database model.
     */

    private fun WeatherData.toDb(lat: String, lon: String): DbWeatherData {
        return DbWeatherData(
            time = Date(),
            locationId = getLocationId(this.now.locationName, this.now.country, lat, lon),
            now = now.toDb(),
            forecastMinutely = forecastMinutely?.map { it.toDb() },
            forecastHourly = forecastHourly?.map { it.toDb() },
            forecastDaily = forecastDaily?.map { it.toDb() }
        )
    }

    private fun WeatherData.WeatherNow.toDb(): DbWeatherData.DbWeatherNow {
        return DbWeatherData.DbWeatherNow(
            time = time,
            locationName = locationName,
            country = country,
            sunrise = sunrise,
            sunset = sunset,
            temperature = temperature,
            feelsLike = feelsLike,
            pressure = pressure,
            humidity = humidity,
            uvIndex = uvIndex,
            windSpeed = windSpeed,
            windDirection = windDirection,
            description = description,
            iconUrl = iconUrl
        )
    }

    private fun WeatherData.WeatherForecastMinutely.toDb(): DbWeatherData.DbWeatherForecastMinutely {
        return DbWeatherData.DbWeatherForecastMinutely(
            time = time,
            precipitation = precipitation
        )
    }

    private fun WeatherData.WeatherForecastHourly.toDb(): DbWeatherData.DbWeatherForecastHourly {
        return DbWeatherData.DbWeatherForecastHourly(
            time = time,
            temperature = temperature,
            feelsLike = feelsLike,
            pressure = pressure,
            humidity = humidity,
            uvIndex = uvIndex,
            windSpeed = windSpeed,
            windDirection = windDirection,
            description = description,
            iconUrl = iconUrl,
            probabilityOfPrecipitation = probabilityOfPrecipitation,
            rainPrecipitation = rainPrecipitation,
            snowPrecipitation = snowPrecipitation
        )
    }

    private fun WeatherData.WeatherForecastDaily.toDb(): DbWeatherData.DbWeatherForecastDaily {
        return DbWeatherData.DbWeatherForecastDaily(
            date = date,
            sunrise = sunrise,
            sunset = sunset,
            summary = summary,
            temperatureDay = temperatureDay,
            temperatureMin = temperatureMin,
            temperatureMax = temperatureMax,
            temperatureNight = temperatureNight,
            temperatureEve = temperatureEve,
            temperatureMorn = temperatureMorn,
            feelsLikeDay = feelsLikeDay,
            feelsLikeNight = feelsLikeNight,
            feelsLikeEve = feelsLikeEve,
            feelsLikeMorn = feelsLikeMorn,
            pressure = pressure,
            humidity = humidity,
            windSpeed = windSpeed,
            windDirection = windDirection,
            description = description,
            iconUrl = iconUrl,
            probabilityOfPrecipitation = probabilityOfPrecipitation,
            rainPrecipitation = rainPrecipitation,
            snowPrecipitation = snowPrecipitation,
            uvIndex = uvIndex
        )
    }

}