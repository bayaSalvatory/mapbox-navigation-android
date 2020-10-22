package com.mapbox.navigation.testing.ui.http

import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.RecordedRequest

interface MockRequestHandler {
    fun handle(request: RecordedRequest): MockResponse?
}
