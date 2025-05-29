package cz.cvut.fit.houdeda2.weather_app.features.settings.di

import cz.cvut.fit.houdeda2.weather_app.features.settings.data.SettingsDataStore
import cz.cvut.fit.houdeda2.weather_app.features.settings.presentation.SettingsViewModel
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val settingsModule = module {

    viewModelOf(::SettingsViewModel)

}