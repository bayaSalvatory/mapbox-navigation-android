package com.mapbox.navigation.instrumentation_tests.utils

import com.mapbox.navigation.core.MapboxNavigationProvider
import org.junit.rules.TestWatcher
import org.junit.runner.Description

class MapboxNavigationRule : TestWatcher() {
    override fun starting(description: Description?) {
        check(MapboxNavigationProvider.isCreated().not())
    }

    override fun finished(description: Description?) {
        MapboxNavigationProvider.destroy()
    }
}
