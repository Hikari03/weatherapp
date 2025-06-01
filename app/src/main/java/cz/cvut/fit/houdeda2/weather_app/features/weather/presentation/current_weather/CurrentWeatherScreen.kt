package cz.cvut.fit.houdeda2.weather_app.features.weather.presentation.current_weather

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.koin.androidx.compose.koinViewModel

@Composable
fun CurrentWeatherScreen(
    viewModel: CurrentWeatherViewModel = koinViewModel()
) {
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


        var text = viewModel.locationStateStream.collectAsStateWithLifecycle()

        Button(
            onClick = {
                viewModel.getGeoForLocation(cityName)
            }
        ) {
            Text("Get Weather")
        }

        if (text.value.location != null) {
            for (location in text.value.location) {
                Log.d(
                    "CurrentWeatherScreen",
                    "Location: ${location.locationName}, ${location.country}"
                )
                Text(
                    text = "Location: ${location.locationName}, ${location.country}" +
                            "\nLatitude: ${location.lat}, Longitude: ${location.lon}",
                    color = MaterialTheme.colorScheme.primary,
                    fontStyle = MaterialTheme.typography.bodyLarge.fontStyle
                )
            }
        }
    }
}