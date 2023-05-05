package com.blinkslabs.blinkist.android.challenge

import com.blinkslabs.blinkist.android.challenge.util.toDateString
import com.blinkslabs.blinkist.android.challenge.util.toLocaleDate
import com.google.common.truth.Truth
import org.junit.Test
import org.threeten.bp.LocalDate

class ExampleTest {

    @Test
    fun testtest() {
        val local = LocalDate.of(2016, 7, 3)


        Truth.assertThat(local.toDateString().toLocaleDate().year).isEqualTo("chkoe")

    }
}
