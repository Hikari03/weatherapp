package cz.cvut.fit.houdeda2.weather_app.features.weather.presentation.forecast

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Refresh
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import cz.cvut.fit.houdeda2.weather_app.R
import cz.cvut.fit.houdeda2.weather_app.features.weather.domain.WeatherData
import cz.cvut.fit.houdeda2.weather_app.features.weather.getTimeFromDate
import cz.cvut.fit.houdeda2.weather_app.features.weather.presentation.CurrentWeatherViewModel
import cz.cvut.fit.houdeda2.weather_app.features.weather.presentation.PrettyCard
import org.koin.androidx.compose.koinViewModel
import java.util.Date

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

    val scrollState = rememberScrollState()

    Column(
        modifier = modifier
            .fillMaxWidth()
            .verticalScroll(scrollState),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = ("Last updated: " + getTimeFromDate(
                weatherData?.now?.time ?: Date()
            )),
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onPrimaryContainer
        )
        Log.d("ForecastScreen", "Weather message: $message")
        if (message != null) {
            val message = message
            Text(
                text = message,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.error
            )
        }
        HorizontalDivider(
            modifier = Modifier.padding(vertical = 4.dp),
            thickness = 1.dp,
            color = MaterialTheme.colorScheme.tertiary
        )

        Minutely(
            minutely = weatherData?.forecastMinutely ?: emptyList(),
            modifier = Modifier.padding(8.dp)
        )

        Hourly(
            hourly = weatherData?.forecastHourly ?: emptyList(),
            modifier = Modifier.padding(8.dp)
        )

        Daily(
            daily = weatherData?.forecastDaily ?: emptyList(),
            modifier = Modifier.padding(8.dp)
        )
    }
}

@Composable
fun Daily(daily: List<WeatherData.WeatherForecastDaily>, modifier: Modifier) {
    PrettyCard(
        modifier = modifier
    ) {
        Column(
            modifier = Modifier.padding(top = 8.dp, bottom = 8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Text(
                text = stringResource(id = R.string.daily_forecast),
                style = MaterialTheme.typography.headlineMedium,
                color = MaterialTheme.colorScheme.onPrimaryContainer
            )

            LazyRow(
                modifier = modifier,
            ) {
                items(daily.size) { index ->
                    val dailyData = daily[index]
                    DailyCard(
                        daily = dailyData,
                        modifier = Modifier.padding(horizontal = 6.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun Hourly(hourly: List<WeatherData.WeatherForecastHourly>, modifier: Modifier) {

    PrettyCard(
        modifier = modifier
    ) {
        Column(
            modifier = Modifier.padding(top = 8.dp, bottom = 8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Text(
                text = stringResource(id = R.string.hourly_forecast),
                style = MaterialTheme.typography.headlineMedium,
                color = MaterialTheme.colorScheme.onPrimaryContainer
            )

            LazyRow(
                modifier = modifier
            ) {
                items(hourly.size) { index ->
                    val minutelyData = hourly[index]
                    HourlyCard(
                        hourly = minutelyData,
                        modifier = Modifier.padding(horizontal = 6.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun Minutely(
    minutely: List<WeatherData.WeatherForecastMinutely>,
    modifier: Modifier = Modifier
) {
    PrettyCard(
        modifier = modifier
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp, bottom = 8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Text(
                text = stringResource(id = R.string.minutely_forecast),
                style = MaterialTheme.typography.headlineMedium,
                color = MaterialTheme.colorScheme.onPrimaryContainer
            )

            LazyRow(
                modifier = modifier
            ) {
                items(minutely.size) { index ->
                    val minutelyData = minutely[index]
                    MinutelyCard(
                        minutely = minutelyData,
                        modifier = Modifier.padding(horizontal = 6.dp)
                    )
                }
            }
        }
    }
}
