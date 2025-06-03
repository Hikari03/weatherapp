package cz.cvut.fit.houdeda2.weather_app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import cz.cvut.fit.houdeda2.weather_app.core.presentation.ui.MainScreen
import cz.cvut.fit.houdeda2.weather_app.core.presentation.ui.theme.AppTheme

lateinit var fusedLocationClient: FusedLocationProviderClient

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        enableEdgeToEdge()
        setContent {
            AppTheme {
                MainScreen()
            }
        }
    }
}