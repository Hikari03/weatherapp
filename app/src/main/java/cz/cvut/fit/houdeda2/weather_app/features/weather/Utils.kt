package cz.cvut.fit.houdeda2.weather_app.features.weather

import cz.cvut.fit.houdeda2.weather_app.R
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

fun getTimeFromDate(date: Date, day: Boolean = false, lineBreak: Boolean = false): String {
    val dayFString = if (day) "EEEE d.M" else ""
    val lineBreakString = if (lineBreak) "\n" else " "

    val formatter = SimpleDateFormat(dayFString + lineBreakString + "HH:mm", Locale.getDefault())
    return formatter.format(date)
}

fun getArrowIconForDirection(degrees: Int): Int {
    return when (degrees) {
        in 0..22, in 337..360 -> R.drawable.rounded_north_24    // North
        in 22..67 -> R.drawable.rounded_north_east_24                   // North-East
        in 67..112 -> R.drawable.rounded_east_24                  // East
        in 112..157 -> R.drawable.rounded_south_east_24                 // South-East
        in 157..202 -> R.drawable.rounded_south_24                 // South
        in 202..247 -> R.drawable.rounded_south_west_24                 // South-West
        in 247..292 -> R.drawable.rounded_west_24                 // West
        in 292..337 -> R.drawable.rounded_north_west_24                 // North-West
        else -> R.drawable.rounded_error_24
    }
}