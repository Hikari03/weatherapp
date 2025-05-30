package cz.cvut.fit.houdeda2.weather_app.features.settings.di

import cz.cvut.fit.houdeda2.weather_app.features.settings.presentation.SettingsViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val settingsModule = module {

    viewModelOf(::SettingsViewModel)

}