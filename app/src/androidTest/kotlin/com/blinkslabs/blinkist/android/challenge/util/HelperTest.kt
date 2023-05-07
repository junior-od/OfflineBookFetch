package com.blinkslabs.blinkist.android.challenge.util

import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.blinkslabs.blinkist.android.challenge.data.local.BookEntity
import com.google.common.truth.Truth
import com.jakewharton.threetenabp.AndroidThreeTen
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.threeten.bp.LocalDate

@RunWith(AndroidJUnit4::class)
class HelperTest {

    @Before
    fun setUp() {
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        AndroidThreeTen.init(appContext.applicationContext)
    }

    /**
     * test previous date  LocalDate.isBeforeToday()
     * returns true
     */
    @Test
    fun testLocalDate_whenPreviousDateIsBeforeToday_Returns_True() {
        val getPreviousLocalDate = LocalDate.now().minusDays(1)

        val result = getPreviousLocalDate.isBeforeToday()

        Truth.assertThat(result).isTrue()
    }

    /**
     * test that today date  LocalDate.isBeforeToday()
     * returns false
     */
    @Test
    fun testLocalDate_whenTodayDateIsBeforeToday_Returns_False() {
        val getPreviousLocalDate = LocalDate.now()

        val result = getPreviousLocalDate.isBeforeToday()

        Truth.assertThat(result).isFalse()
    }

    /**
     * test that future date  LocalDate.isBeforeToday()
     * returns false
     */
    @Test
    fun testLocalDate_whenFutureDateIsBeforeToday_Returns_False() {
        val getPreviousLocalDate = LocalDate.now().plusDays(1)

        val result = getPreviousLocalDate.isBeforeToday()

        Truth.assertThat(result).isFalse()
    }

    /**
     * test BookEntity?.shouldGetTodaysUpdate
     * that bookEntity is null returns true
     * */
    @Test
    fun testBookEntityShouldGetTodaysUpdate_whenBookEntityIsNull_Returns_True() {
        val bookEntity: BookEntity? = null

        val result = bookEntity.shouldGetTodaysUpdate()

        Truth.assertThat(result).isTrue()
    }

    /**
     * test BookEntity?.shouldGetTodaysUpdate
     * that when bookEntity date created field
     * is previous date returns true
     */
    @Test
    fun testBookEntityShouldGetTodaysUpdate_whenBookEntityHasPreviousDateCreated_Returns_True() {
        val bookEntity: BookEntity?

        bookEntity = BookEntity(
            id = "1",
            name = "test bok",
            author = "test author",
            publishDate = "2019-12-12",
            coverImageUrl = "cover image",
            dateCreated = LocalDate.now().minusDays(1).toDateString()
        )

        val result = bookEntity.shouldGetTodaysUpdate()

        Truth.assertThat(result).isTrue()
    }

    /**
     * test BookEntity?.shouldGetTodaysUpdate
     * that when bookEntity date created field
     * is today date returns false
     */
    @Test
    fun testBookEntityShouldGetTodaysUpdate_whenBookEntityHasTodayDateCreated_Returns_False() {
        val bookEntity: BookEntity?

        bookEntity = BookEntity(
            id = "1",
            name = "test bok",
            author = "test author",
            publishDate = "2019-12-12",
            coverImageUrl = "cover image",
            dateCreated = LocalDate.now().toDateString()
        )

        val result = bookEntity.shouldGetTodaysUpdate()

        Truth.assertThat(result).isFalse()
    }

    /**
     * test BookEntity?.shouldGetTodaysUpdate
     * that when bookEntity date created field
     * is future date returns false
     */
    @Test
    fun testBookEntityShouldGetTodaysUpdate_whenBookEntityHasFutureDateCreated_Returns_False() {
        val bookEntity: BookEntity?

        bookEntity = BookEntity(
            id = "1",
            name = "test bok",
            author = "test author",
            publishDate = "2019-12-12",
            coverImageUrl = "cover image",
            dateCreated = LocalDate.now().plusDays(1).toDateString()
        )

        val result = bookEntity.shouldGetTodaysUpdate()

        Truth.assertThat(result).isFalse()
    }
}
