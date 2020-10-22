package com.mapbox.navigation.testing.ui

import androidx.appcompat.app.AppCompatActivity
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.rule.ActivityTestRule
import androidx.test.uiautomator.UiDevice
import com.mapbox.navigation.testing.ui.http.MockWebServerRule
import org.junit.Rule

open class BaseTest<A : AppCompatActivity>(activityClass: Class<A>) {

    @get:Rule
    val activityRule = ActivityTestRule(activityClass)

    @get:Rule
    val mockLocationUpdatesRule = MockLocationUpdatesRule()

    @get:Rule
    val mockWebServerRule =
        MockWebServerRule()

    protected val uiDevice = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation())

    val activity: A
        get() = activityRule.activity

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
