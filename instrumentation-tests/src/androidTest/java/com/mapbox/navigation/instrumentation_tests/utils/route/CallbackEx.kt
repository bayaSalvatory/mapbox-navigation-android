package com.mapbox.navigation.instrumentation_tests.utils.route

import com.mapbox.api.directions.v5.models.DirectionsRoute
import com.mapbox.api.directions.v5.models.RouteOptions
import com.mapbox.navigation.core.directions.session.RoutesRequestCallback

fun routesRequestCallback(
    onRoutesReady: ((routes: List<DirectionsRoute>) -> Unit)? = null,
    onRoutesRequestFailure: ((throwable: Throwable, routeOptions: RouteOptions) -> Unit)? = null,
    onRoutesRequestCanceled: ((routeOptions: RouteOptions) -> Unit)? = null
): RoutesRequestCallback = object : RoutesRequestCallback {
    override fun onRoutesReady(routes: List<DirectionsRoute>) {
        onRoutesReady?.invoke(routes)
    }

    override fun onRoutesRequestFailure(throwable: Throwable, routeOptions: RouteOptions) {
        onRoutesRequestFailure?.invoke(throwable, routeOptions)
    }

    override fun onRoutesRequestCanceled(routeOptions: RouteOptions) {
        onRoutesRequestCanceled?.invoke(routeOptions)
    }
}
