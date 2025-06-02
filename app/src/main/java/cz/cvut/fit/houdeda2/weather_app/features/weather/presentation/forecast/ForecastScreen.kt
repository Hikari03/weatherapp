package cz.cvut.fit.houdeda2.weather_app.features.weather.presentation.forecast

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
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
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.key
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import cz.cvut.fit.houdeda2.weather_app.R
import cz.cvut.fit.houdeda2.weather_app.features.weather.domain.WeatherData
import cz.cvut.fit.houdeda2.weather_app.features.weather.presentation.CurrentWeatherViewModel
import cz.cvut.fit.houdeda2.weather_app.features.weather.presentation.PrettyCard
import cz.cvut.fit.houdeda2.weather_app.features.weather.presentation.current_weather.getArrowIconForDirection
import cz.cvut.fit.houdeda2.weather_app.features.weather.presentation.current_weather.getTimeFromDate
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
                modifier = modifier
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
fun DailyCard(daily: WeatherData.WeatherForecastDaily, modifier: Modifier) {

    val scrollState = rememberScrollState()

    PrettyCard(
        modifier = modifier.size(width = 200.dp, height = 440.dp).verticalScroll(scrollState)
    ) {

        Column(
            modifier = modifier
                .fillMaxWidth()
                .padding(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = getTimeFromDate(daily.date),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.tertiary
            )

            HorizontalDivider(
                modifier = Modifier
                    .padding(vertical = 8.dp).size(width = 80.dp, height = 1.dp),
                thickness = 1.dp,
                color = MaterialTheme.colorScheme.tertiary
            )

            AsyncImage(
                modifier = Modifier.size(100.dp),
                contentScale = ContentScale.Fit,
                model = daily.iconUrl,
                contentDescription = stringResource(R.string.weather_icon),
            )

            Text(
                modifier = Modifier.size(width = 100.dp, height = 40.dp),
                text = daily.summary,
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onPrimaryContainer,
                textAlign = TextAlign.Center
            )

            HorizontalDivider(
                modifier = Modifier
                    .padding(vertical = 8.dp).size(width = 130.dp, height = 1.dp),
                thickness = 1.dp,
                color = MaterialTheme.colorScheme.secondary
            )

            DisplayTemp(
                temperature = daily.temperatureDay,
                feelsLike = daily.feelsLikeDay
            )

            HorizontalDivider(
                modifier = Modifier
                    .padding(vertical = 8.dp).size(width = 130.dp, height = 1.dp),
                thickness = 1.dp,
                color = MaterialTheme.colorScheme.secondary
            )

            DisplayOther(
                pressure = daily.pressure,
                humidity = daily.humidity,
                uvIndex = daily.uvIndex,
                windSpeed = daily.windSpeed,
                windDirection = daily.windDirection
            )

            HorizontalDivider(
                modifier = Modifier
                    .padding(vertical = 8.dp).size(width = 130.dp, height = 1.dp),
                thickness = 1.dp,
                color = MaterialTheme.colorScheme.secondary
            )

            DisplayPrecipitation(
                probabilityOfPrecipitation = daily.probabilityOfPrecipitation,
                rainPrecipitation = daily.rainPrecipitation,
                snowPrecipitation = daily.snowPrecipitation
            )

            HorizontalDivider(
                modifier = Modifier
                    .padding(vertical = 8.dp).size(width = 130.dp, height = 1.dp),
                thickness = 1.dp,
                color = MaterialTheme.colorScheme.secondary
            )

            DisplayTempThroughoutDay(
                daily = daily
            )
        }
    }
}

@Composable
fun DisplayTempThroughoutDay(daily: WeatherData.WeatherForecastDaily) {
    Text(
        text = stringResource(id = R.string.temperature_throughout_day),
        style = MaterialTheme.typography.headlineSmall,
        color = MaterialTheme.colorScheme.onPrimaryContainer,
        textDecoration = TextDecoration.Underline
    )

    HorizontalDivider(
        modifier = Modifier
            .padding(vertical = 8.dp).size(width = 100.dp, height = 1.dp),
        thickness = 1.dp,
        color = MaterialTheme.colorScheme.onPrimary
    )

    Text(
        text = "Min/Max\n${daily.temperatureMin}°C / ${daily.temperatureMax}°C",
        style = MaterialTheme.typography.bodyLarge,
        color = MaterialTheme.colorScheme.onPrimaryContainer,
        textAlign = TextAlign.Center
    )

    HorizontalDivider(
        modifier = Modifier
            .padding(vertical = 8.dp).size(width = 50.dp, height = 1.dp),
        thickness = 1.dp,
        color = MaterialTheme.colorScheme.onPrimary
    )

    Text(
        text = "Morning: ${daily.temperatureMorn}°C, Evening: ${daily.temperatureEve}°C, Night: ${daily.temperatureNight}°C",
        style = MaterialTheme.typography.bodyLarge,
        color = MaterialTheme.colorScheme.onPrimaryContainer,
        textAlign = TextAlign.Center,
        overflow = TextOverflow.Ellipsis
    )

    HorizontalDivider(
        modifier = Modifier
            .padding(vertical = 8.dp).size(width = 50.dp, height = 1.dp),
        thickness = 1.dp,
        color = MaterialTheme.colorScheme.onPrimary
    )

    Text(
        text = "Feels like\nMorning: ${daily.feelsLikeMorn}°C, Evening: ${daily.feelsLikeEve}°C, Night: ${daily.feelsLikeNight}°C",
        style = MaterialTheme.typography.bodyLarge,
        color = MaterialTheme.colorScheme.onPrimaryContainer,
        textAlign = TextAlign.Center,
        overflow = TextOverflow.Ellipsis
    )

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
fun HourlyCard(hourly: WeatherData.WeatherForecastHourly, modifier: Modifier) {

    PrettyCard(
        modifier = modifier
    ) {

        Column(
            modifier = modifier
                .fillMaxWidth()
                .padding(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            DisplayTimeIcon(
                time = hourly.time,
                iconUrl = hourly.iconUrl
            )

            Text(
                modifier = Modifier.size(width = 100.dp, height = 40.dp),
                text = hourly.description,
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onPrimaryContainer,
                textAlign = TextAlign.Center
            )

            HorizontalDivider(
                modifier = Modifier
                    .padding(vertical = 8.dp)
                    .size(width = 130.dp, height = 1.dp),
                thickness = 1.dp,
                color = MaterialTheme.colorScheme.tertiary
            )

            DisplayTemp(
                temperature = hourly.temperature,
                feelsLike = hourly.feelsLike
            )

            HorizontalDivider(
                modifier = Modifier
                    .padding(vertical = 8.dp)
                    .size(width = 130.dp, height = 1.dp),
                thickness = 1.dp,
                color = MaterialTheme.colorScheme.secondary
            )

            DisplayOther(
                pressure = hourly.pressure,
                humidity = hourly.humidity,
                uvIndex = hourly.uvIndex,
                windSpeed = hourly.windSpeed,
                windDirection = hourly.windDirection
            )

            HorizontalDivider(
                modifier = Modifier
                    .padding(vertical = 8.dp)
                    .size(width = 130.dp, height = 1.dp),
                thickness = 1.dp,
                color = MaterialTheme.colorScheme.secondary
            )

            Log.d(
                "HourlyCard",
                "rain: ${hourly.rainPrecipitation}, snow: ${hourly.snowPrecipitation}"
            )

            DisplayPrecipitation(
                probabilityOfPrecipitation = hourly.probabilityOfPrecipitation,
                rainPrecipitation = hourly.rainPrecipitation,
                snowPrecipitation = hourly.snowPrecipitation
            )

        }
    }
}

@Composable
fun RowIconText(
    painter: Painter,
    contentDescription: String,
    text: String,
    textStyle: TextStyle,
    iconSize: Dp = 24.dp
) {
    Row {
        IconText(
            painter = painter,
            contentDescription = contentDescription,
            text = text,
            textStyle = textStyle,
            iconSize = iconSize
        )
    }
}

@Composable
fun IconText(
    painter: Painter,
    contentDescription: String,
    text: String,
    textStyle: TextStyle,
    iconSize: Dp = 24.dp
) {
    Icon(
        painter = painter,
        contentDescription = contentDescription,
        modifier = Modifier
            .padding(end = 4.dp)
            .size(iconSize),
        tint = MaterialTheme.colorScheme.onPrimaryContainer
    )
    Text(
        text = text,
        style = textStyle,
        color = MaterialTheme.colorScheme.onPrimaryContainer
    )
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

@Composable
fun MinutelyCard(
    minutely: WeatherData.WeatherForecastMinutely,
    modifier: Modifier = Modifier
) {

    PrettyCard(
        modifier
    ) {

        Column(
            modifier = modifier
                .fillMaxWidth()
                .padding(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Text(
                text = getTimeFromDate(minutely.time),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.tertiary
            )

            HorizontalDivider(
                modifier = Modifier
                    .padding(vertical = 8.dp)
                    .size(width = 80.dp, height = 1.dp),
                thickness = 1.dp,
                color = MaterialTheme.colorScheme.tertiary
            )

            Text(
                text = "${minutely.precipitation} mm/h",
                style = MaterialTheme.typography.headlineSmall,
                color = MaterialTheme.colorScheme.onPrimaryContainer
            )
        }
    }
}

@Composable
fun DisplayTimeIcon(
    time: Date,
    iconUrl: String
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {

        Text(
            text = getTimeFromDate(time),
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.tertiary
        )

        VerticalDivider(
            modifier = Modifier
                .padding(horizontal = 8.dp)
                .size(width = 1.dp, height = 24.dp),
            thickness = 1.dp,
            color = MaterialTheme.colorScheme.tertiary
        )

        AsyncImage(
            modifier = Modifier.size(46.dp),
            contentScale = ContentScale.Fit,
            model = iconUrl,
            contentDescription = stringResource(R.string.weather_icon),
        )


    }
}

@Composable
fun DisplayTemp(
    temperature: Double,
    feelsLike: Double
) {
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        RowIconText(
            painter = painterResource(R.drawable.thermometer_39),
            contentDescription = stringResource(R.string.temp_icon),
            text = "${temperature}°C",
            textStyle = MaterialTheme.typography.headlineSmall
        )
        RowIconText(
            painter = painterResource(R.drawable.person_24),
            contentDescription = stringResource(R.string.feels_like_icon),
            text = "${feelsLike}°C",
            textStyle = MaterialTheme.typography.bodyLarge,
            iconSize = 20.dp
        )
    }
}

@Composable
fun DisplayOther(
    pressure: Double,
    humidity: Int,
    uvIndex: Double,
    windSpeed: Double,
    windDirection: Int
) {
    RowIconText(
        painter = painterResource(R.drawable.compress_24),
        contentDescription = stringResource(R.string.pressure_icon),
        text = "$pressure hPa",
        textStyle = MaterialTheme.typography.bodyLarge,
        iconSize = 20.dp
    )
    RowIconText(
        painter = painterResource(R.drawable.water_drop_24),
        contentDescription = stringResource(R.string.humidity_icon),
        text = "$humidity%",
        textStyle = MaterialTheme.typography.bodyLarge,
        iconSize = 20.dp
    )


    Row {
        Icon(
            painter = painterResource(R.drawable.air_24),
            contentDescription = stringResource(R.string.wind_icon),
            modifier = Modifier
                .padding(end = 4.dp)
                .size(20.dp),
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
            modifier = Modifier
                .padding(end = 4.dp)
                .size(20.dp),
            tint = MaterialTheme.colorScheme.onPrimaryContainer
        )
    }

    RowIconText(
        painter = painterResource(R.drawable.uv_index),
        contentDescription = stringResource(R.string.uv_index_icon),
        text = "$uvIndex",
        textStyle = MaterialTheme.typography.bodyLarge,
        iconSize = 20.dp
    )
}

@Composable
fun DisplayPrecipitation(
    probabilityOfPrecipitation: Double,
    rainPrecipitation: Double,
    snowPrecipitation: Double
) {

    val probabilityText = if (probabilityOfPrecipitation > 0) {
        "${(probabilityOfPrecipitation*100).toInt()}%"
    } else {
        "0%"
    }

    RowIconText(
        painter = painterResource(R.drawable.pop),
        contentDescription = stringResource(R.string.probability_of_precipitation_icon),
        text = probabilityText,
        textStyle = MaterialTheme.typography.bodyLarge,
        iconSize = 20.dp
    )

    if (rainPrecipitation > 0.0) {
        RowIconText(
            painter = painterResource(R.drawable.rainy_24),
            contentDescription = stringResource(R.string.rain_icon),
            text = "$rainPrecipitation mm/h",
            textStyle = MaterialTheme.typography.bodyLarge,
            iconSize = 20.dp
        )
    }

    if (snowPrecipitation > 0.0) {
        RowIconText(
            painter = painterResource(R.drawable.weather_snowy_24),
            contentDescription = stringResource(R.string.snow_icon),
            text = "$snowPrecipitation mm/h",
            textStyle = MaterialTheme.typography.bodyLarge,
            iconSize = 20.dp
        )
    }
}
