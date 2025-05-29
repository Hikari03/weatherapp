package cz.cvut.fit.houdeda2.weather_app.core.presentation.ui

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import cz.cvut.fit.houdeda2.weather_app.R
import kotlinx.serialization.Serializable

sealed interface Screens {

    sealed interface TopLevel : Screens {
        @get:DrawableRes
        val icon: Int

        @get:StringRes
        val title: Int

        @Serializable
        data object CurrentWeather : TopLevel {
            override val title: Int
                get() = R.string.current_weather

            override val icon: Int
                get() = R.drawable.rounded_partly_cloudy_day_24
        }

        @Serializable
        data object Forecast : TopLevel {
            override val title: Int
                get() = R.string.forecast

            override val icon: Int
                get() = R.drawable.rounded_fast_forward_24
        }

        @Serializable
        data object Settings : TopLevel {
            override val title: Int
                get() = R.string.settings

            override val icon: Int
                get() = R.drawable.rounded_settings_24
        }

        companion object {
            val all get() = listOf(CurrentWeather, Forecast, Settings)
        }
    }
}

    