package cz.cvut.fit.houdeda2.weather_app.core.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import cz.cvut.fit.houdeda2.weather_app.features.weather.data.db.Converters
import cz.cvut.fit.houdeda2.weather_app.features.weather.data.db.DbWeatherData
import cz.cvut.fit.houdeda2.weather_app.features.weather.data.db.WeatherDao

@Database(
    version = 1,
    entities = [DbWeatherData::class]
)
@TypeConverters(Converters::class)
abstract class WeatherDatabase : RoomDatabase() {
    abstract fun weatherDao(): WeatherDao

    companion object {

        fun newInstance(context: Context): WeatherDatabase {
            return Room.databaseBuilder(
                context,
                WeatherDatabase::class.java,
                "weather.db"
            ).build()
        }
    }
}