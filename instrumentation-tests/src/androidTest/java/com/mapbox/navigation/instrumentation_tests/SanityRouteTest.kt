package com.mapbox.navigation.instrumentation_tests

import android.util.Log
import androidx.test.espresso.Espresso
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.mapbox.api.directions.v5.models.DirectionsRoute
import com.mapbox.api.directions.v5.models.RouteOptions
import com.mapbox.geojson.Point
import com.mapbox.navigation.base.internal.extensions.applyDefaultParams
import com.mapbox.navigation.base.internal.extensions.coordinates
import com.mapbox.navigation.base.trip.model.RouteProgress
import com.mapbox.navigation.core.MapboxNavigation
import com.mapbox.navigation.core.MapboxNavigationProvider
import com.mapbox.navigation.core.directions.session.RoutesRequestCallback
import com.mapbox.navigation.core.replay.MapboxReplayer
import com.mapbox.navigation.core.replay.route.ReplayRouteMapper
import com.mapbox.navigation.core.trip.session.RouteProgressObserver
import com.mapbox.navigation.instrumentation_tests.activity.EmptyTestActivity
import com.mapbox.navigation.instrumentation_tests.utils.Utils
import com.mapbox.navigation.testing.ui.BaseTest
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class SanityRouteTest : BaseTest<EmptyTestActivity>(EmptyTestActivity::class.java) {

    private lateinit var mapboxNavigation: MapboxNavigation
    private val mapper = ReplayRouteMapper()
    private val mapboxReplayer = MapboxReplayer()

    @Before
    fun setup() {
        Espresso.onIdle()
        check(MapboxNavigationProvider.isCreated().not())

        val options = MapboxNavigation.defaultNavigationOptionsBuilder(
            activity,
            Utils.getMapboxAccessToken(activity)!!
        ).build()
        mapboxNavigation = MapboxNavigationProvider.create(options)
    }

    @Test
    fun route_completes() {
        uiDevice.run {
            mapboxNavigation.startTripSession()
            mapboxNavigation.requestRoutes(
                RouteOptions.builder().applyDefaultParams()
                    .accessToken(Utils.getMapboxAccessToken(activity)!!)
                    .coordinates(
                        origin = Point.fromLngLat(-77.031969, 38.894113),
                        destination = Point.fromLngLat(-77.028064, 38.895848)
                    ).build(),
                object : RoutesRequestCallback {
                    override fun onRoutesReady(routes: List<DirectionsRoute>) {
                        val replayEvents = mapper.mapDirectionsRouteGeometry(routes[0])
                        mapboxReplayer.pushEvents(replayEvents)
                        mapboxReplayer.seekTo(replayEvents.first())
                        mapboxReplayer.play()
                    }

                    override fun onRoutesRequestFailure(
                        throwable: Throwable,
                        routeOptions: RouteOptions
                    ) {
                        throw RuntimeException()
                    }

                    override fun onRoutesRequestCanceled(routeOptions: RouteOptions) {
                        throw RuntimeException()
                    }
                }
            )
            mapboxNavigation.registerRouteProgressObserver(object : RouteProgressObserver {
                override fun onRouteProgressChanged(routeProgress: RouteProgress) {
                    Log.e("TESTSETSE", "progress state: " + routeProgress.currentState)
                }
            })
        }

        Thread.sleep(20000)
    }

    @After
    fun teardown() {
        MapboxNavigationProvider.destroy()
    }
}
