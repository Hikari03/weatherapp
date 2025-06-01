package cz.cvut.fit.houdeda2.weather_app

import android.app.Application
import android.content.Context
import cz.cvut.fit.houdeda2.weather_app.core.di.appModule
import cz.cvut.fit.houdeda2.weather_app.features.settings.di.settingsModule
import cz.cvut.fit.houdeda2.weather_app.features.weather.di.weatherModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.GlobalContext.startKoin

private lateinit var application: Application
val appContext: Context get() = application


class App : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@App)
            modules(appModule, settingsModule, weatherModule)
        }
        application = this@App
    }
}