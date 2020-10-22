package com.mapbox.navigation.core.replay

import android.location.LocationManager
import com.mapbox.navigation.core.replay.history.ReplayEventBase
import com.mapbox.navigation.core.replay.history.ReplayEventUpdateLocation
import com.mapbox.navigation.core.replay.history.ReplayEventsObserver
import com.mapbox.navigation.core.replay.utils.toLocation

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
        val location = event.toLocation()
        locationManager.setTestProviderLocation(location.provider, location)
    }
}
