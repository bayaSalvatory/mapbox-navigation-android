package com.mapbox.navigation.core.replay

import android.location.Location
import android.location.LocationManager
import android.os.Build
import android.os.SystemClock
import com.mapbox.navigation.core.replay.history.ReplayEventBase
import com.mapbox.navigation.core.replay.history.ReplayEventUpdateLocation
import com.mapbox.navigation.core.replay.history.ReplayEventsObserver
import java.util.Date

class ReplayMockLocation(
    mapboxReplayer: MapboxReplayer,
    private val locationManager: LocationManager
) : ReplayEventsObserver {

    init {
        mapboxReplayer.registerObserver(this)
    }

    override fun replayEvents(events: List<ReplayEventBase>) {
        events.forEach { event ->
            when (event) {
                is ReplayEventUpdateLocation -> replayLocation(event)
            }
        }
    }

    private fun replayLocation(event: ReplayEventUpdateLocation) {
        val eventLocation = event.location
        val location = Location(eventLocation.provider)
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

        locationManager.setTestProviderLocation(location.provider, location)
    }
}
