package com.mapbox.navigation.testing.ui

import android.content.Context
import android.location.Location
import android.location.LocationManager
import android.os.Build
import android.os.SystemClock
import androidx.test.core.app.ApplicationProvider
import androidx.test.platform.app.InstrumentationRegistry.getInstrumentation
import org.junit.rules.TestWatcher
import org.junit.runner.Description
import java.util.Date

private const val mockProviderName = LocationManager.GPS_PROVIDER

class MockLocationUpdatesRule : TestWatcher() {

    private val instrumentation = getInstrumentation()
    private val appContext = (ApplicationProvider.getApplicationContext() as Context)

    // lm.getBestProvider(Criteria().also { it.accuracy = Criteria.ACCURACY_FINE }, true)
    private val locationManager: LocationManager by lazy {
        (appContext.getSystemService(Context.LOCATION_SERVICE) as LocationManager).also {
            try {
                it.removeTestProvider(mockProviderName)
            } finally {
                it.addTestProvider(
                    mockProviderName,
                    false,
                    false,
                    false,
                    false,
                    true,
                    true,
                    true,
                    3,
                    2
                )
                it.setTestProviderEnabled(mockProviderName, true)
            }
        }
    }

    override fun starting(description: Description?) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            with(instrumentation.uiAutomation) {
                val result = executeShellCommand(
                    "appops set " +
                        appContext.packageName +
                        " android:mock_location allow"
                )
            }
        } else {
            throw RuntimeException("MockLocationUpdatesRule is supported on version codes >= Build.VERSION_CODES.M")
        }
    }

    /**
     * @param modifyFn allows to modify the base location
     */
    fun generateLocationUpdate(modifyFn: (Location.() -> Unit)? = null): Location {
        val location = Location(mockProviderName)
        location.time = Date().time
        location.elapsedRealtimeNanos = SystemClock.elapsedRealtimeNanos()
        location.accuracy = 5f
        location.altitude = 0.0
        location.bearing = 0f
        location.speed = 5f
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            location.verticalAccuracyMeters = 5f
            location.bearingAccuracyDegrees = 5f
            location.speedAccuracyMetersPerSecond = 5f
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            location.elapsedRealtimeUncertaintyNanos = 0.0
        }

        if (modifyFn != null) {
            location.apply(modifyFn)
        }

        return location
    }

    fun pushLocationUpdate(location: Location) {
        check(location.provider == mockProviderName) {
            """
              location provider "${location.provider}" is not equal to required "$mockProviderName"
            """.trimIndent()
        }
        locationManager.setTestProviderLocation(mockProviderName, location)
    }
}
