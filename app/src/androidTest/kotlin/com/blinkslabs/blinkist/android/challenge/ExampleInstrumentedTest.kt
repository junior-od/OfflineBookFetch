package com.blinkslabs.blinkist.android.challenge

import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.google.common.truth.Truth
import org.junit.Assert.*
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class ExampleInstrumentedTest {

    @Test
    fun testPackageName() {
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        val expected = "com.blinkslabs.blinkist.android.challenge"
        Truth.assertThat(appContext.packageName).isEqualTo(expected)
    }
}
