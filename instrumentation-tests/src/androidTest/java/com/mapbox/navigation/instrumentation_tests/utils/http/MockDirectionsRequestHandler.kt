package com.mapbox.navigation.instrumentation_tests.utils.http

import com.mapbox.geojson.Point
import com.mapbox.navigation.testing.ui.http.MockRequestHandler
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.RecordedRequest

data class MockDirectionsRequestHandler(
    val profile: String,
    val jsonResponse: String,
    val coordinates: List<Point>?
) : MockRequestHandler {
    override fun handle(request: RecordedRequest): MockResponse? {
        return if (request.path.startsWith("""/directions/v5/mapbox/$profile/${coordinates.parseCoordinates()}""")) {
            MockResponse().setBody(jsonResponse)
        } else {
            null
        }
    }

    private fun List<Point>?.parseCoordinates(): String {
        if (this == null) {
            return ""
        }

        return this.joinToString(";") { "${it.longitude()},${it.latitude()}" }
    }
}
