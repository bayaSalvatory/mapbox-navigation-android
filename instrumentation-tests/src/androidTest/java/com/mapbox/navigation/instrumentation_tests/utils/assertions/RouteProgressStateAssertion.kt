package com.mapbox.navigation.instrumentation_tests.utils.assertions

import com.mapbox.navigation.base.trip.model.RouteProgress
import com.mapbox.navigation.base.trip.model.RouteProgressState
import com.mapbox.navigation.core.MapboxNavigation
import com.mapbox.navigation.core.trip.session.RouteProgressObserver
import com.mapbox.navigation.instrumentation_tests.utils.assertions.ExpectedRouteProgressState.Optional
import com.mapbox.navigation.instrumentation_tests.utils.assertions.ExpectedRouteProgressState.Required
import org.junit.Assert

// todo make this generic for a condition rather than route progress state and then specilaize the implementaiton
class RouteProgressStateAssertion(
    mapboxNavigation: MapboxNavigation,
    expected: () -> List<ExpectedRouteProgressState>
) : NavigationAssertion {
    val states = mutableListOf<RouteProgressState>()
    private val expectedStates =
        mutableListOf<ExpectedRouteProgressState>().apply { addAll(expected()) }

    init {
        mapboxNavigation.registerRouteProgressObserver(object : RouteProgressObserver {
            override fun onRouteProgressChanged(routeProgress: RouteProgress) {
                states.add(routeProgress.currentState)
            }
        })
    }

    override fun assert() {
        var minIndex = 0
        states.forEach { state ->
            val minRequiredIndex =
                expectedStates.indexOfFirst { it is Required && expectedStates.indexOf(it) > minIndex }
            val availableStates =
                expectedStates.filterIndexed { index, _ ->
                    if (minRequiredIndex >= 0) {
                        index in minIndex..minRequiredIndex
                    } else {
                        index >= minIndex
                    }
                }
            when (val matching = availableStates.find { it.state == state }) {
                is Required -> {
                    minIndex = expectedStates.indexOf(matching)
                }
                is Optional -> {
                    // do nothing
                }
                null -> Assert.fail(
                    """$state shouldn't be returned right now. Expected are:
                    |$availableStates
                """.trimMargin()
                )
            }
        }
        Assert.assertEquals(
            """Not all of the expected states were matched. Below were not matched:
               |${expectedStates.drop(minIndex).filterIsInstance<Required>()}
            """.trimMargin(),
            expectedStates.indexOfLast { it is Required },
            minIndex
        )
    }
}

sealed class ExpectedRouteProgressState(val state: RouteProgressState) {
    class Required(state: RouteProgressState) :
        ExpectedRouteProgressState(state)

    class Optional(state: RouteProgressState) :
        ExpectedRouteProgressState(state)

    override fun toString(): String {
        return "${this::class.simpleName}(state=$state)"
    }
}

fun requiredState(
    routeProgressState: RouteProgressState
): ExpectedRouteProgressState = Required(routeProgressState)

fun optionalState(
    routeProgressState: RouteProgressState
): ExpectedRouteProgressState = Optional(routeProgressState)
