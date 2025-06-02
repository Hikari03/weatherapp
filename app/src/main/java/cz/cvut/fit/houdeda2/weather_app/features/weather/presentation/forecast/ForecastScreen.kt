package cz.cvut.fit.houdeda2.weather_app.features.weather.presentation.forecast

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Refresh
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.key
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import cz.cvut.fit.houdeda2.weather_app.R
import cz.cvut.fit.houdeda2.weather_app.features.weather.domain.WeatherData
import cz.cvut.fit.houdeda2.weather_app.features.weather.presentation.CurrentWeatherViewModel
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ForecastScreen(
    viewModel: CurrentWeatherViewModel = koinViewModel()
) {
    val weatherState = viewModel.weatherStateStream.collectAsStateWithLifecycle()
    LaunchedEffect(Unit) {
        viewModel.getWeather()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = stringResource(id = R.string.forecast),
                        style = MaterialTheme.typography.headlineMedium,
                        color = MaterialTheme.colorScheme.primary
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.onPrimary
                ),
                actions = {
                    IconButton(onClick = {
                        viewModel.getWeather()
                    }) {
                        Icon(
                            imageVector = Icons.Rounded.Refresh,
                            contentDescription = stringResource(id = R.string.refresh_weather),
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                }
            )
        },
        containerColor = MaterialTheme.colorScheme.inversePrimary,
    ) { paddingValues ->
        key(weatherState.value.weatherData) {
            WeatherForecastContent(
                weatherData = weatherState.value.weatherData,
                message = weatherState.value.message,
                modifier = Modifier.padding(paddingValues)
            )
        }
    }
}

@Composable
fun WeatherForecastContent(
    weatherData: WeatherData?,
    message: String?,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.padding(16.dp) // Add padding to the content
    ) {

    }
}