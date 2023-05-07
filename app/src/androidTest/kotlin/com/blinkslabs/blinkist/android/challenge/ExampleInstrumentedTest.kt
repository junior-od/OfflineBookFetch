package com.blinkslabs.blinkist.android.challenge


import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.blinkslabs.blinkist.android.challenge.util.isBeforeToday
import com.google.common.truth.Truth
import com.jakewharton.threetenabp.AndroidThreeTen
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.threeten.bp.LocalDate

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class ExampleInstrumentedTest {

    @Before
    fun setUp(){
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        AndroidThreeTen.init(appContext.applicationContext)
    }

    @Test
    fun useAppContext() {
        val local = LocalDate.of(2023, 5, 8)
        // Context of the app under test.
        Truth.assertThat(local.isBeforeToday()).isTrue()
    }
}
