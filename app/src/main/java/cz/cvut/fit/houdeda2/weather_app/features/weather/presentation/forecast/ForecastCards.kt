package cz.cvut.fit.houdeda2.weather_app.features.weather.presentation.forecast

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import cz.cvut.fit.houdeda2.weather_app.R
import cz.cvut.fit.houdeda2.weather_app.features.weather.domain.WeatherData
import cz.cvut.fit.houdeda2.weather_app.features.weather.getTimeFromDate
import cz.cvut.fit.houdeda2.weather_app.features.weather.presentation.DisplayOther
import cz.cvut.fit.houdeda2.weather_app.features.weather.presentation.DisplayPrecipitation
import cz.cvut.fit.houdeda2.weather_app.features.weather.presentation.DisplayTemp
import cz.cvut.fit.houdeda2.weather_app.features.weather.presentation.DisplayTempThroughoutDay
import cz.cvut.fit.houdeda2.weather_app.features.weather.presentation.DisplayTimeIconHourly
import cz.cvut.fit.houdeda2.weather_app.features.weather.presentation.PrettyCard

/*
* For the ForecastScreen, we have three types of cards:
* 1. MinutelyCard - displays minute-by-minute precipitation data.
* 2. HourlyCard - displays hourly weather data.
* 3. DailyCard - displays daily weather data.
 */


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

            DisplayTimeIconHourly(
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
                iconSize = 20.dp,
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
fun DailyCard(daily: WeatherData.WeatherForecastDaily, modifier: Modifier) {

    PrettyCard(
        modifier = modifier.size(width = 200.dp, height = 730.dp)
    ) {

        Column(
            modifier = modifier
                .fillMaxWidth()
                .padding(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = getTimeFromDate(daily.date, true),
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
                    .padding(vertical = 8.dp)
                    .size(width = 130.dp, height = 1.dp),
                thickness = 1.dp,
                color = MaterialTheme.colorScheme.secondary
            )

            DisplayTemp(
                temperature = daily.temperatureDay,
                feelsLike = daily.feelsLikeDay
            )

            HorizontalDivider(
                modifier = Modifier
                    .padding(vertical = 8.dp)
                    .size(width = 130.dp, height = 1.dp),
                thickness = 1.dp,
                color = MaterialTheme.colorScheme.secondary
            )

            DisplayOther(
                iconSize = 20.dp,
                pressure = daily.pressure,
                humidity = daily.humidity,
                uvIndex = daily.uvIndex,
                windSpeed = daily.windSpeed,
                windDirection = daily.windDirection
            )

            HorizontalDivider(
                modifier = Modifier
                    .padding(vertical = 8.dp)
                    .size(width = 130.dp, height = 1.dp),
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
                    .padding(vertical = 8.dp)
                    .size(width = 130.dp, height = 1.dp),
                thickness = 1.dp,
                color = MaterialTheme.colorScheme.secondary
            )

            DisplayTempThroughoutDay(
                daily = daily
            )
        }
    }
}