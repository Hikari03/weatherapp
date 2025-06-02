package cz.cvut.fit.houdeda2.weather_app.features.weather.di

import cz.cvut.fit.houdeda2.weather_app.core.data.db.WeatherDatabase
import cz.cvut.fit.houdeda2.weather_app.features.weather.data.WeatherRepository
import cz.cvut.fit.houdeda2.weather_app.features.weather.data.api.WeatherRemoteDataSource
import cz.cvut.fit.houdeda2.weather_app.features.weather.data.db.WeatherLocalDataSource
import cz.cvut.fit.houdeda2.weather_app.features.weather.presentation.current_weather.CurrentWeatherViewModel
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val weatherModule = module {
    factoryOf(::WeatherRemoteDataSource)
    single { get<WeatherDatabase>().weatherDao() }
    factoryOf(::WeatherLocalDataSource)
    factoryOf(::WeatherRepository)

    viewModelOf(::CurrentWeatherViewModel)
}