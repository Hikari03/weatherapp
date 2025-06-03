package cz.cvut.fit.houdeda2.weather_app.core.data

import android.Manifest
import android.location.Location
import android.util.Log
import androidx.annotation.RequiresPermission
import com.google.android.gms.location.CurrentLocationRequest
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.Priority
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

class LocationProvider(
    private val fusedLocationProviderClient: FusedLocationProviderClient
) {

    @RequiresPermission(anyOf = [Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION])
    suspend fun getLocation(): Location? {
        return try {
            val locationRequest = CurrentLocationRequest.Builder()
                .setPriority(Priority.PRIORITY_LOW_POWER)
                .setDurationMillis(500L)
                .setMaxUpdateAgeMillis(10_000L)
                .build()
            return suspendCoroutine { continuation ->
                fusedLocationProviderClient.lastLocation
                    .addOnSuccessListener { location ->
                        continuation.resume(location)
                    }
                    .addOnFailureListener { e ->
                        continuation.resumeWithException(e)
                    }
            }
        } catch (e: Exception) {
            Log.e("LocationProvider", "Error getting location", e)
            null
        }
    }

}