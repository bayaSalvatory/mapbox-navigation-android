package com.mapbox.navigation.instrumentation_tests.utils.location

import android.location.LocationManager
import com.mapbox.api.directions.v5.models.DirectionsRoute
import com.mapbox.navigation.core.replay.MapboxReplayer
import com.mapbox.navigation.core.replay.history.ReplayEventBase
import com.mapbox.navigation.core.replay.history.ReplayEventUpdateLocation
import com.mapbox.navigation.core.replay.history.ReplayEventsObserver
import com.mapbox.navigation.core.replay.route.ReplayRouteMapper
import com.mapbox.navigation.core.replay.utils.toLocation
import com.mapbox.navigation.testing.ui.MockLocationUpdatesRule
import org.junit.rules.TestWatcher
import org.junit.runner.Description

class MockLocationReplayerRule(mockLocationUpdatesRule: MockLocationUpdatesRule): TestWatcher() {
    private val replayEventsObserver = object : ReplayEventsObserver {
        override fun replayEvents(events: List<ReplayEventBase>) {
            events.forEach {
                if (it is ReplayEventUpdateLocation) {
                    mockLocationUpdatesRule.pushLocationUpdate(it.toLocation(LocationManager.GPS_PROVIDER))
                }
            }
        }
    }
    private val mapper = ReplayRouteMapper()
    var mapboxReplayer: MapboxReplayer? = null

    override fun starting(description: Description?) {
        mapboxReplayer = MapboxReplayer().also {
            it.registerObserver(replayEventsObserver)
        }
    }

    override fun finished(description: Description?) {
        mapboxReplayer?.finish()
        mapboxReplayer = null
    }

    fun playRoute(directionsRoute: DirectionsRoute) {
        val replayEvents = mapper.mapDirectionsRouteGeometry(directionsRoute)
        mapboxReplayer?.clearEvents()
        mapboxReplayer?.pushEvents(replayEvents)
        mapboxReplayer?.seekTo(replayEvents.first())
        mapboxReplayer?.play()
    }
}
