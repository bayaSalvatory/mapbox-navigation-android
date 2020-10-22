package com.mapbox.navigation.instrumentation_tests.utils.route

import com.mapbox.api.directions.v5.models.DirectionsRoute
import com.mapbox.api.directions.v5.models.RouteOptions
import com.mapbox.navigation.core.directions.session.RoutesRequestCallback

class RoutesRequestCallbackAdapter: RoutesRequestCallback {
    override fun onRoutesReady(routes: List<DirectionsRoute>) {
        // no impl
    }

    override fun onRoutesRequestFailure(throwable: Throwable, routeOptions: RouteOptions) {
        // no impl
    }

    override fun onRoutesRequestCanceled(routeOptions: RouteOptions) {
        // no impl
    }
}
