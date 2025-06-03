package cz.cvut.fit.houdeda2.weather_app.features.weather.presentation.current_weather

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
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
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.key
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import cz.cvut.fit.houdeda2.weather_app.R
import cz.cvut.fit.houdeda2.weather_app.features.weather.domain.WeatherData
import cz.cvut.fit.houdeda2.weather_app.features.weather.presentation.CurrentWeatherViewModel
import cz.cvut.fit.houdeda2.weather_app.features.weather.presentation.PrettyCard
import org.koin.androidx.compose.koinViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CurrentWeatherScreen(
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
                        text = stringResource(id = R.string.current_weather),
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
            Column(
                modifier = Modifier
                    .padding(paddingValues)
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = ("Last updated: " + getTimeFromDate(
                        weatherState.value.weatherData?.now?.time ?: Date()
                    )),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )
                Log.d("CurrentWeatherScreen", "Weather message: ${weatherState.value.message}")
                if (weatherState.value.message != null) {
                    val message = weatherState.value.message ?: ""
                    Text(
                        text = message,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.error
                    )
                }

                CurrentWeather(weatherState.value.weatherData?.now)
            }
        }
    }
}

@Composable
fun CurrentWeather(
    weatherNow: WeatherData.WeatherNow? = null
) {

    if (weatherNow == null) {
        Text(
            text = stringResource(id = R.string.loading_weather),
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.padding(16.dp)
        )
        return
    }
    Log.i("CurrentWeather", "Weather data: $weatherNow")

    Text(
        text = weatherNow.locationName + ", " + weatherNow.country,
        style = MaterialTheme.typography.headlineLarge,
        fontSize = 40.sp,
        modifier = Modifier.padding(16.dp),
        color = MaterialTheme.colorScheme.onPrimaryContainer,
        textDecoration = TextDecoration.Underline,
    )

    TempCard(weatherNow, modifier = Modifier.padding(top = 12.dp))
    WeatherDetailsCards(weatherNow, modifier = Modifier.padding(top = 12.dp))
}


// Displays the current temperature card with weather information
@Composable
fun TempCard(
    weatherNow: WeatherData.WeatherNow,
    modifier: Modifier = Modifier
) {
    PrettyCard(
        modifier = modifier,
    ) {
        Column(
            modifier = Modifier.padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Row {
                AsyncImage(
                    model = weatherNow.iconUrl,
                    contentDescription = stringResource(R.string.weather_icon),
                    modifier = Modifier.size(50.dp)
                )
                Text(
                    text = weatherNow.description.replaceFirstChar { it.uppercase() },
                    style = MaterialTheme.typography.bodyLarge,
                    fontSize = 20.sp,
                    modifier = Modifier
                        .padding(start = 8.dp, end = 8.dp)
                        .align(Alignment.CenterVertically),
                    color = MaterialTheme.colorScheme.onPrimaryContainer

                )
            }

            Text(
                text = weatherNow.temperature.toString() + "°C",
                style = MaterialTheme.typography.headlineLarge,
                fontSize = 70.sp,
                color = MaterialTheme.colorScheme.onPrimaryContainer
            )

            Text(
                text = "Feels Like: " + weatherNow.feelsLike.toString() + "°C",
                style = MaterialTheme.typography.bodyLarge,
                fontSize = 20.sp,
                color = MaterialTheme.colorScheme.secondary
            )

        }
    }
}

// Displays sunrise, sunset, and other weather details in a card
@Composable
fun WeatherDetailsCards(
    weatherNow: WeatherData.WeatherNow,
    modifier: Modifier = Modifier
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(0.dp),
    ) {
        PrettyCard(
            modifier = modifier
        ) {
            DisplaySunriseAndSunset(
                sunrise = weatherNow.sunrise,
                sunset = weatherNow.sunset
            )
        }


        PrettyCard(
            modifier = modifier
        ) {
            DisplayPressure(pressure = weatherNow.pressure)
        }

        PrettyCard(
            modifier = modifier
        ) {
            DisplayHumidity(humidity = weatherNow.humidity)
        }

        PrettyCard(
            modifier = modifier
        ) {
            DisplayWind(
                windSpeed = weatherNow.windSpeed,
                windDirection = weatherNow.windDirection
            )
        }
        PrettyCard(
            modifier = modifier
        ) {
            DisplayUVIndex(
                uvIndex = weatherNow.uvIndex,
            )
        }
    }
}

@Composable
fun DisplayUVIndex(uvIndex: Double) {

    Row(
        modifier = Modifier.padding(8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(2.dp)
    ) {

        Icon(
            painter = painterResource(R.drawable.uv_index),
            contentDescription = stringResource(R.string.uv_index_icon),
            modifier = Modifier.padding(end = 4.dp),
            tint = MaterialTheme.colorScheme.onPrimaryContainer
        )

        Text(
            text = uvIndex.toString(),
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onPrimaryContainer
        )
    }
}


@Composable
fun DisplaySunriseAndSunset(
    sunrise: Date,
    sunset: Date
) {
    Row(
        modifier = Modifier.padding(8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(2.dp)
    ) {
        Icon(
            painter = painterResource(R.drawable.wb_twilight_24),
            contentDescription = stringResource(R.string.sunrise_sunset_icon),
            modifier = Modifier.padding(end = 4.dp),
            tint = MaterialTheme.colorScheme.onPrimaryContainer
        )

        Text(
            text = getTimeFromDate(sunrise),
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onPrimaryContainer
        )

        VerticalDivider(
            modifier = Modifier
                .padding(horizontal = 8.dp)
                .size(height = 24.dp, width = 1.dp),
            color = MaterialTheme.colorScheme.tertiary,
            thickness = 1.dp
        )

        Text(
            text = getTimeFromDate(sunset),
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onPrimaryContainer
        )
    }
}

@Composable
fun DisplayPressure(
    pressure: Double
) {
    Row(
        modifier = Modifier.padding(8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(2.dp)
    ) {
        Icon(
            painter = painterResource(R.drawable.compress_24),
            contentDescription = stringResource(R.string.pressure_icon),
            modifier = Modifier.padding(end = 4.dp),
            tint = MaterialTheme.colorScheme.onPrimaryContainer
        )

        Text(
            text = "$pressure hPa",
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onPrimaryContainer
        )
    }
}

@Composable
fun DisplayHumidity(
    humidity: Int
) {
    Row(
        modifier = Modifier.padding(8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(2.dp)
    ) {
        Icon(
            painter = painterResource(R.drawable.water_drop_24),
            contentDescription = stringResource(R.string.humidity_icon),
            modifier = Modifier.padding(end = 4.dp),
            tint = MaterialTheme.colorScheme.onPrimaryContainer
        )

        Text(
            text = "$humidity%",
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onPrimaryContainer
        )
    }
}

@Composable
fun DisplayWind(
    windSpeed: Double,
    windDirection: Int
) {
    Row(
        modifier = Modifier.padding(8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(2.dp)
    ) {
        Icon(
            painter = painterResource(R.drawable.air_24),
            contentDescription = stringResource(R.string.wind_icon),
            modifier = Modifier.padding(end = 4.dp),
            tint = MaterialTheme.colorScheme.onPrimaryContainer
        )

        Text(
            text = "$windSpeed m/s",
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onPrimaryContainer
        )

        VerticalDivider(
            modifier = Modifier
                .padding(horizontal = 8.dp)
                .size(height = 24.dp, width = 1.dp),
            color = MaterialTheme.colorScheme.tertiary,
            thickness = 1.dp
        )

        Icon(
            painter = painterResource(getArrowIconForDirection(windDirection)),
            contentDescription = stringResource(R.string.wind_direction_icon),
            modifier = Modifier.padding(end = 4.dp),
            tint = MaterialTheme.colorScheme.onPrimaryContainer
        )
    }
}


fun getTimeFromDate(date: Date): String {
    val formatter = SimpleDateFormat("hh:mm a", Locale.getDefault())
    return formatter.format(date)
}

fun getArrowIconForDirection(degrees: Int): Int {
    return when (degrees) {
        in 0..22, in 337..360 -> R.drawable.rounded_north_24    // North
        in 22..67 -> R.drawable.rounded_north_east_24                   // North-East
        in 67..112 -> R.drawable.rounded_east_24                  // East
        in 112..157 -> R.drawable.rounded_south_east_24                 // South-East
        in 157..202 -> R.drawable.rounded_south_24                 // South
        in 202..247 -> R.drawable.rounded_south_west_24                 // South-West
        in 247..292 -> R.drawable.rounded_west_24                 // West
        in 292..337 -> R.drawable.rounded_north_west_24                 // North-West
        else -> R.drawable.rounded_error_24
    }
}



