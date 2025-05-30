package cz.cvut.fit.houdeda2.weather_app.features.weather.presentation.current_weather

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import cz.cvut.fit.houdeda2.weather_app.core.data.api.ApiClient
import cz.cvut.fit.houdeda2.weather_app.features.weather.data.WeatherRepository
import cz.cvut.fit.houdeda2.weather_app.features.weather.data.api.WeatherRemoteDataSource
import kotlinx.coroutines.runBlocking

@Composable
fun CurrentWeatherScreen() {
    Column {
        Text("Current Weather Screen")

        // input field for city name
        var cityName = "Prague" // Example city name, can be replaced with a state variable
        TextField(
            value = cityName,
            onValueChange = { cityName = it },
            label = { Text("Enter city name") },
            placeholder = { Text("e.g. Prague") }
        )

        val weatherRepository =
            WeatherRepository(WeatherRemoteDataSource(ApiClient())) // Assuming you have a WeatherRepository class

        var text = ""

        Button(
            onClick = {

                runBlocking {
                    text = "data: $cityName\n" + "weather: ${
                        weatherRepository.getGeoForLocation(cityName)
                    }"
                    Log.d("CurrentWeatherScreen", "Button clicked, weather data: $text")
                }
            }
        ) {
            Text("Get Weather")
        }
    }
}