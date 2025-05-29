package cz.cvut.fit.houdeda2.weather_app.core.presentation.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import cz.cvut.fit.houdeda2.weather_app.features.settings.presentation.SettingsScreen
import cz.cvut.fit.houdeda2.weather_app.features.weather.presentation.current_weather.CurrentWeatherScreen
import cz.cvut.fit.houdeda2.weather_app.features.weather.presentation.forecast.ForecastScreen

@Composable
fun Navigation(
    navController: NavHostController,
    modifier: Modifier = Modifier,
) {
    NavHost(
        modifier = modifier,
        navController = navController,
        startDestination = Screens.TopLevel.CurrentWeather
    ) {
        composable<Screens.TopLevel.CurrentWeather> {
            CurrentWeatherScreen(navController)
        }

        composable<Screens.TopLevel.Forecast> {
            ForecastScreen(navController)
        }

        composable<Screens.TopLevel.Settings> {
            SettingsScreen()
        }
    }

}