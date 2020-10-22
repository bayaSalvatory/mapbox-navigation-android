package com.mapbox.navigation.core.replay.utils

import android.location.Location
import android.os.SystemClock
import com.mapbox.navigation.core.replay.history.ReplayEventUpdateLocation
import java.util.Date

fun ReplayEventUpdateLocation.toLocation(
    provider: String = this.location.provider ?: "Replay"
): Location {
    val eventLocation = this.location
    val location = Location(provider)
    location.longitude = eventLocation.lon
    location.latitude = eventLocation.lat
    location.time = Date().time
    location.elapsedRealtimeNanos = SystemClock.elapsedRealtimeNanos()
    eventLocation.accuracyHorizontal?.toFloat()?.let { location.accuracy = it }
    eventLocation.bearing?.toFloat()?.let { location.bearing = it }
    eventLocation.altitude?.let { location.altitude = it }
    eventLocation.speed?.toFloat()?.let { location.speed = it }

    /*if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        location.verticalAccuracyMeters = 5f
        location.bearingAccuracyDegrees = 5f
        location.speedAccuracyMetersPerSecond = 5f
    }
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
        location.elapsedRealtimeUncertaintyNanos = 0.0
    }*/
    return location
}
