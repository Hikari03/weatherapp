package cz.cvut.fit.houdeda2.weather_app.features.weather.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import cz.cvut.fit.houdeda2.weather_app.R
import cz.cvut.fit.houdeda2.weather_app.features.weather.domain.WeatherData
import cz.cvut.fit.houdeda2.weather_app.features.weather.getArrowIconForDirection
import cz.cvut.fit.houdeda2.weather_app.features.weather.getTimeFromDate
import java.util.Date

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
            .padding(vertical = 8.dp)
            .size(width = 100.dp, height = 1.dp),
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
            .padding(vertical = 8.dp)
            .size(width = 50.dp, height = 1.dp),
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
            .padding(vertical = 8.dp)
            .size(width = 50.dp, height = 1.dp),
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
fun DisplayTimeIconHourly(
    time: Date,
    iconUrl: String
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {

        Text(
            text = getTimeFromDate(time, true, true),
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
    modifier: Modifier = Modifier,
    horizontalArrangement: Arrangement.Horizontal = Arrangement.Start,
    verticalAlignment: Alignment.Vertical = Alignment.Top,
    iconSize: Dp,
    pressure: Double,
    humidity: Int,
    uvIndex: Double,
    windSpeed: Double,
    windDirection: Int
) {
    Row(
        modifier = modifier,
        horizontalArrangement = horizontalArrangement,
        verticalAlignment = verticalAlignment,
    ) {
        Icon(
            painter = painterResource(R.drawable.air_24),
            contentDescription = stringResource(R.string.wind_icon),
            modifier = Modifier
                .padding(end = 4.dp)
                .size(iconSize),
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
                .size(iconSize),
            tint = MaterialTheme.colorScheme.onPrimaryContainer
        )
    }

    RowIconText(
        modifier = modifier,
        horizontalArrangement = horizontalArrangement,
        verticalAlignment = verticalAlignment,
        painter = painterResource(R.drawable.compress_24),
        contentDescription = stringResource(R.string.pressure_icon),
        text = "$pressure hPa",
        textStyle = MaterialTheme.typography.bodyLarge,
        iconSize = iconSize
    )

    RowIconText(
        modifier = modifier,
        horizontalArrangement = horizontalArrangement,
        verticalAlignment = verticalAlignment,
        painter = painterResource(R.drawable.uv_index),
        contentDescription = stringResource(R.string.uv_index_icon),
        text = "$uvIndex",
        textStyle = MaterialTheme.typography.bodyLarge,
        iconSize = iconSize
    )

    RowIconText(
        modifier = modifier,
        horizontalArrangement = horizontalArrangement,
        verticalAlignment = verticalAlignment,
        painter = painterResource(R.drawable.water_drop_24),
        contentDescription = stringResource(R.string.humidity_icon),
        text = "$humidity%",
        textStyle = MaterialTheme.typography.bodyLarge,
        iconSize = iconSize
    )

}

@Composable
fun DisplayPrecipitation(
    probabilityOfPrecipitation: Double,
    rainPrecipitation: Double,
    snowPrecipitation: Double
) {

    val probabilityText = if (probabilityOfPrecipitation > 0) {
        "${(probabilityOfPrecipitation * 100).toInt()}%"
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