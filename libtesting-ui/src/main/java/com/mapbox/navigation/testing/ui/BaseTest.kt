package com.mapbox.navigation.testing.ui

import androidx.appcompat.app.AppCompatActivity
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.uiautomator.UiDevice
import com.schibsted.spain.barista.rule.BaristaRule
import org.junit.Before
import org.junit.Rule

open class BaseTest<A : AppCompatActivity>(activityClass: Class<A>) {

    @get:Rule
    val activityRule = BaristaRule.create(activityClass)

    @get:Rule
    val mockLocationUpdatesRule = MockLocationUpdatesRule()

    protected val uiDevice = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation())

    @Before
    fun setUp() {
        activityRule.launchActivity()
    }

    val activity: A
        get() = activityRule.activityTestRule.activity

    val appName: String by lazy {
        val applicationInfo = activity.applicationInfo
        val stringId = applicationInfo.labelRes

        if (stringId == 0) {
            applicationInfo.nonLocalizedLabel.toString()
        } else {
            activity.getString(stringId)
        }
    }
}
