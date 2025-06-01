package cz.cvut.fit.houdeda2.weather_app.core.di

import cz.cvut.fit.houdeda2.weather_app.core.data.api.ApiClient
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val appModule = module {
    singleOf(::ApiClient)
}