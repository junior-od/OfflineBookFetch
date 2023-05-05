package com.blinkslabs.blinkist.android.challenge

import com.google.common.truth.Truth
import org.junit.Test
import org.threeten.bp.LocalDate
import org.threeten.bp.temporal.WeekFields

import java.util.Locale

class ExampleTest {

    @Test
    fun testtest() {
        val local = LocalDate.of(2016, 7, 3)
        val weekOfYear = local.get(WeekFields.of(Locale.getDefault()).weekOfYear())

        Truth.assertThat(weekOfYear).isEqualTo(local.year)

    }
}
