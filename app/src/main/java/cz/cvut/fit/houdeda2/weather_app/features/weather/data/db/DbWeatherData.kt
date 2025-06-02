package cz.cvut.fit.houdeda2.weather_app.features.weather.data.db

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import java.util.Date

@Entity(tableName = "weather_data")
data class DbWeatherData(
    @PrimaryKey val time: Date,
    val locationId: String,
    val now: DbWeatherNow,
    val forecastMinutely: List<DbWeatherForecastMinutely>? = null,
    val forecastHourly: List<DbWeatherForecastHourly>? = null,
    val forecastDaily: List<DbWeatherForecastDaily>? = null
) {
    @Serializable
    data class DbWeatherNow(
        @Serializable(with = DateSerializer::class)
        val time: Date,
        val locationName: String,
        val country: String,
        @Serializable(with = DateSerializer::class)
        val sunrise: Date,
        @Serializable(with = DateSerializer::class)
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

    @Serializable
    data class DbWeatherForecastMinutely(
        @Serializable(with = DateSerializer::class)
        val time: Date,
        val precipitation: Double,
    )

    @Serializable
    data class DbWeatherForecastHourly(
        @Serializable(with = DateSerializer::class)
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

    @Serializable
    data class DbWeatherForecastDaily(
        @Serializable(with = DateSerializer::class)
        val date: Date,
        @Serializable(with = DateSerializer::class)
        val sunrise: Date,
        @Serializable(with = DateSerializer::class)
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

object DateSerializer : KSerializer<Date> {
    override val descriptor = PrimitiveSerialDescriptor(
        serialName = "Date",
        kind = PrimitiveKind.LONG
    )

    override fun serialize(encoder: Encoder, value: Date) {
        encoder.encodeLong(value.time)
    }

    override fun deserialize(decoder: Decoder): Date {
        return Date(decoder.decodeLong())
    }
}