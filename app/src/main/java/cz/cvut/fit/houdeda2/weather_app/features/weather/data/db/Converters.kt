package cz.cvut.fit.houdeda2.weather_app.features.weather.data.db


import androidx.room.TypeConverter
import kotlinx.serialization.json.Json
import java.util.Date

class Converters {

    @TypeConverter
    fun fromDate(date: Date): Long {
        return date.time
    }

    @TypeConverter
    fun toDate(timestamp: Long): Date {
        return Date(timestamp)
    }

    @TypeConverter
    fun fromWeatherNow(value: DbWeatherData.DbWeatherNow): String = Json.encodeToString(value)


    @TypeConverter
    fun toWeatherNow(value: String): DbWeatherData.DbWeatherNow = Json.decodeFromString(value)

    @TypeConverter
    fun fromWeatherForecastMinutely(value: List<DbWeatherData.DbWeatherForecastMinutely>?): String? {
        return value?.let { Json.encodeToString(it) }
    }

    @TypeConverter
    fun toWeatherForecastMinutely(value: String?): List<DbWeatherData.DbWeatherForecastMinutely>? {
        return value?.let { Json.decodeFromString(it) }
    }

    @TypeConverter
    fun fromWeatherForecastHourly(value: List<DbWeatherData.DbWeatherForecastHourly>?): String? {
        return value?.let { Json.encodeToString(it) }
    }

    @TypeConverter
    fun toWeatherForecastHourly(value: String?): List<DbWeatherData.DbWeatherForecastHourly>? {
        return value?.let { Json.decodeFromString(it) }
    }

    @TypeConverter
    fun fromWeatherForecastDaily(value: List<DbWeatherData.DbWeatherForecastDaily>?): String? {
        return value?.let { Json.encodeToString(it) }
    }

    @TypeConverter
    fun toWeatherForecastDaily(value: String?): List<DbWeatherData.DbWeatherForecastDaily>? {
        return value?.let { Json.decodeFromString(it) }
    }
}