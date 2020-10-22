package com.mapbox.navigation.instrumentation_tests.utils.assertions

fun navAssert(assertion: NavigationAssertion) = assertion.assert()

interface NavigationAssertion {
    fun assert()
}
