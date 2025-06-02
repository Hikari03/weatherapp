package cz.cvut.fit.houdeda2.weather_app.features.weather.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface WeatherDao {

    // Newest weather data for a specific location
    @Query("SELECT * FROM weather_data WHERE locationId = :locationId ORDER BY time DESC LIMIT 1")
    suspend fun getLatestWeather(locationId: String): DbWeatherData?

    @Insert
    suspend fun insertWeatherData(weatherData: DbWeatherData)

}