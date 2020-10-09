package com.mapbox.navigation.instrumentation_tests

import androidx.test.espresso.Espresso
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.mapbox.navigation.core.MapboxNavigation
import com.mapbox.navigation.core.MapboxNavigationProvider
import com.mapbox.navigation.instrumentation_tests.activity.EmptyTestActivity
import com.mapbox.navigation.testing.ui.BaseTest
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class SanityRouteTest: BaseTest<EmptyTestActivity>(EmptyTestActivity::class.java) {

    private lateinit var mapboxNavigation: MapboxNavigation

    @Before
    fun setup() {
        Espresso.onIdle()
        check(MapboxNavigationProvider.isCreated().not())

        val options =
            MapboxNavigation.defaultNavigationOptionsBuilder(
                activity,

                )
        mapboxNavigation =
            MapboxNavigationProvider.create()
    }

    @Test
    fun route_completes() {

    }
}
