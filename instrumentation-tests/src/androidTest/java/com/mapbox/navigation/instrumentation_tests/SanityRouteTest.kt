package com.mapbox.navigation.instrumentation_tests

import android.util.Log
import androidx.test.espresso.Espresso
import com.mapbox.api.directions.v5.models.RouteOptions
import com.mapbox.geojson.Point
import com.mapbox.navigation.base.internal.extensions.applyDefaultParams
import com.mapbox.navigation.base.internal.extensions.coordinates
import com.mapbox.navigation.base.trip.model.RouteProgress
import com.mapbox.navigation.base.trip.model.RouteProgressState
import com.mapbox.navigation.core.MapboxNavigation
import com.mapbox.navigation.core.MapboxNavigationProvider
import com.mapbox.navigation.core.trip.session.RouteProgressObserver
import com.mapbox.navigation.instrumentation_tests.activity.EmptyTestActivity
import com.mapbox.navigation.instrumentation_tests.utils.MapboxNavigationRule
import com.mapbox.navigation.instrumentation_tests.utils.Utils
import com.mapbox.navigation.instrumentation_tests.utils.assertions.RouteProgressStateAssertion
import com.mapbox.navigation.instrumentation_tests.utils.assertions.navAssert
import com.mapbox.navigation.instrumentation_tests.utils.assertions.optionalState
import com.mapbox.navigation.instrumentation_tests.utils.assertions.requiredState
import com.mapbox.navigation.instrumentation_tests.utils.http.MockDirectionsRequestHandler
import com.mapbox.navigation.instrumentation_tests.utils.location.MockLocationReplayerRule
import com.mapbox.navigation.instrumentation_tests.utils.route.routesRequestCallback
import com.mapbox.navigation.testing.ui.BaseTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class SanityRouteTest : BaseTest<EmptyTestActivity>(EmptyTestActivity::class.java) {

    @get:Rule
    val mapboxNavigationRule = MapboxNavigationRule()

    @get:Rule
    val mockLocationReplayerRule = MockLocationReplayerRule(mockLocationUpdatesRule)

    private lateinit var mapboxNavigation: MapboxNavigation

    @Before
    fun setup() {
        Espresso.onIdle()

        val options = MapboxNavigation.defaultNavigationOptionsBuilder(
            activity,
            Utils.getMapboxAccessToken(activity)!!
        ).build()
        mapboxNavigation = MapboxNavigationProvider.create(options)
    }

    @Test
    fun route_completes() {
        mockWebServerRule.requestHandlers.add(
            MockDirectionsRequestHandler(
                "driving",
                """{"routes":[{"weight_name":"auto","weight":39.92,"duration":30.045,"distance":174.356,"legs":[{"steps":[{"voiceInstructions":[{"ssmlAnnouncement":"<speak><amazon:effect name=\"drc\"><prosody rate=\"1.08\">Drive north on 14th Street Northwest. Then Turn right onto Pennsylvania Avenue Northwest.</prosody></amazon:effect></speak>","announcement":"Drive north on 14th Street Northwest. Then Turn right onto Pennsylvania Avenue Northwest.","distanceAlongGeometry":80.356}],"bannerInstructions":[{"primary":{"components":[{"type":"text","text":"Pennsylvania Avenue Northwest"}],"type":"turn","modifier":"right","text":"Pennsylvania Avenue Northwest"},"distanceAlongGeometry":80.356}],"maneuver":{"type":"depart","instruction":"Drive north on 14th Street Northwest.","bearing_after":0,"bearing_before":0,"location":[-77.031952,38.894722]},"intersections":[{"entry":[true],"bearings":[0],"duration":8.17,"mapbox_streets_v8":{"class":"secondary"},"is_urban":true,"admin_index":0,"out":0,"weight":9.804,"geometry_index":0,"location":[-77.031952,38.894722]},{"bearings":[0,180],"entry":[true,false],"in":1,"turn_weight":2,"turn_duration":2,"mapbox_streets_v8":{"class":"secondary"},"is_urban":true,"admin_index":0,"out":0,"geometry_index":1,"location":[-77.031952,38.895355]}],"weight":13.198,"duration":11.332,"distance":80.356,"name":"14th Street Northwest","driving_side":"right","mode":"driving","geometry":"ag}diA~_t|qCsf@?uD?"},{"voiceInstructions":[{"ssmlAnnouncement":"<speak><amazon:effect name=\"drc\"><prosody rate=\"1.08\">You have arrived at your destination.</prosody></amazon:effect></speak>","announcement":"You have arrived at your destination.","distanceAlongGeometry":79.167}],"bannerInstructions":[{"primary":{"components":[{"type":"text","text":"You will arrive at your destination"}],"type":"arrive","modifier":"straight","text":"You will arrive at your destination"},"distanceAlongGeometry":94},{"primary":{"components":[{"type":"text","text":"You have arrived at your destination"}],"type":"arrive","modifier":"straight","text":"You have arrived at your destination"},"distanceAlongGeometry":79.167}],"maneuver":{"type":"turn","instruction":"Turn right onto Pennsylvania Avenue Northwest.","modifier":"right","bearing_after":90,"bearing_before":0,"location":[-77.031952,38.895447]},"intersections":[{"entry":[true,false],"in":1,"bearings":[90,180],"duration":6.661,"turn_weight":9,"turn_duration":4,"mapbox_streets_v8":{"class":"primary"},"is_urban":true,"admin_index":0,"out":0,"weight":12.26,"geometry_index":2,"location":[-77.031952,38.895447]},{"entry":[true,false],"in":1,"bearings":[90,270],"duration":7.043,"mapbox_streets_v8":{"class":"primary"},"is_urban":true,"admin_index":0,"out":0,"weight":8.452,"geometry_index":3,"location":[-77.031754,38.895447]},{"bearings":[90,270],"entry":[true,false],"in":1,"mapbox_streets_v8":{"class":"primary"},"is_urban":true,"admin_index":0,"out":0,"geometry_index":4,"location":[-77.031235,38.895447]}],"weight":26.722,"duration":18.713,"distance":94,"name":"Pennsylvania Avenue Northwest","driving_side":"right","mode":"driving","geometry":"kt~diA~_t|qC?kK?m_@?{U"},{"voiceInstructions":[],"bannerInstructions":[],"maneuver":{"type":"arrive","instruction":"You have arrived at your destination.","bearing_after":0,"bearing_before":90,"location":[-77.030869,38.895447]},"intersections":[{"bearings":[270],"entry":[true],"in":0,"admin_index":0,"geometry_index":5,"location":[-77.030869,38.895447]}],"weight":0,"duration":0,"distance":0,"name":"Pennsylvania Avenue Northwest","driving_side":"right","mode":"driving","geometry":"kt~diAh|q|qC??"}],"admins":[{"iso_3166_1_alpha3":"USA","iso_3166_1":"US"}],"duration":30.045,"annotation":{"congestion":["unknown","unknown","unknown","unknown","unknown"],"distance":[70.5,10.2,17.2,44.9,31.7]},"distance":174.356,"weight":39.92,"summary":"14th Street Northwest, Pennsylvania Avenue Northwest"}],"geometry":"ag}diA~_t|qCsf@?uD??kK?m_@?{U","voiceLocale":"en-US"}],"waypoints":[{"distance":3.306,"name":"14th Street Northwest","location":[-77.031952,38.894722]},{"distance":4.929,"name":"Pennsylvania Avenue Northwest","location":[-77.030869,38.895447]}],"code":"Ok","uuid":"Fa41LujP_13srK0hmJuy9XsXjwJKCZTsMiCK8i4ku1Pu5SYzHYfvYw=="}""",
                listOf(
                    Point.fromLngLat(-77.031991, 38.894721),
                    Point.fromLngLat(-77.030923, 38.895433)
                )
            )
        )

        val statesVerification = RouteProgressStateAssertion(mapboxNavigation) {
            listOf(
                optionalState(RouteProgressState.ROUTE_INVALID),
                requiredState(RouteProgressState.LOCATION_TRACKING),
                requiredState(RouteProgressState.ROUTE_COMPLETE)
            )
        }
        uiDevice.run {
            mockLocationUpdatesRule.pushLocationUpdate {
                latitude = 38.894721
                longitude = -77.031991
            }
            mapboxNavigation.startTripSession()
            mapboxNavigation.requestRoutes(
                RouteOptions.builder().applyDefaultParams()
                    .baseUrl(mockWebServerRule.baseUrl)
                    .accessToken(Utils.getMapboxAccessToken(activity)!!)
                    .coordinates(
                        origin = Point.fromLngLat(-77.031991, 38.894721),
                        destination = Point.fromLngLat(-77.030923, 38.895433)
                    ).build(),
                routesRequestCallback(
                    onRoutesReady = { mockLocationReplayerRule.playRoute(it[0]) }
                )
            )
            mapboxNavigation.registerRouteProgressObserver(object : RouteProgressObserver {
                override fun onRouteProgressChanged(routeProgress: RouteProgress) {
                    Log.e("TESTSETSE", "progress state: " + routeProgress.currentState)
                }
            })
        }

        Thread.sleep(20000)
        navAssert(statesVerification)
    }
}
