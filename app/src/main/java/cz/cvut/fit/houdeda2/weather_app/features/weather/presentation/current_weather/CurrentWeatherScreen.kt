package cz.cvut.fit.houdeda2.weather_app.features.weather.presentation.current_weather

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
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


    }
}

