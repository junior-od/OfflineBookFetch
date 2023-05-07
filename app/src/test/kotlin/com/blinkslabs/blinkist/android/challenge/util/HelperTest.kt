package com.blinkslabs.blinkist.android.challenge.util

import com.blinkslabs.blinkist.android.challenge.data.model.Book
import com.google.common.truth.Truth
import org.junit.Test
import org.threeten.bp.LocalDate

class HelperTest {

    /**
     * test that groupBooksByDatesInSameWeek with empty
     * book list param
     * should return empty map
     **/
    @Test
    fun `test groupBooksByDatesInSameWeek with empty book list return empty map`() {
        val booksList = emptyList<Book>()

        val resultMap = groupBooksByDatesInSameWeek(booksList)

        val expectedMap = emptyMap<String, List<Book>>()
        Truth.assertThat(resultMap).isEqualTo(expectedMap)
    }

    /**
     * test that groupBooksByDatesInSameWeek
     * with book list param should return the correct
     * mapped books by same week in the year
     */

    @Test
    fun `test groupBooksByDatesInSameWeek with book list should return the correct mapped books by week`() {
        val booksList = listOf(
            Book(
                "1",
                "book 1",
                "author 1",
                LocalDate.of(2018, 7, 3),
                "cover url"
            ),
            Book(
                "2",
                "book 2",
                "book 2",
                LocalDate.of(2018, 7, 2),
                "cover url"
            ),
            Book(
                "3",
                "book 3",
                "book 3",
                LocalDate.of(2018, 6, 19),
                "cover url"
            )
        )

        val resultMap = groupBooksByDatesInSameWeek(booksList)

        val expectedMap = mapOf(
            "Week 27, 2018" to listOf(
                Book(
                    "1",
                    "book 1",
                    "author 1",
                    LocalDate.of(2018, 7, 3),
                    "cover url"
                ),
                Book(
                    "2",
                    "book 2",
                    "book 2",
                    LocalDate.of(2018, 7, 2),
                    "cover url"
                )
            ),
            "Week 25, 2018" to listOf(
                Book(
                    "3",
                    "book 3",
                    "book 3",
                    LocalDate.of(2018, 6, 19),
                    "cover url"
                )
            )
        )

        Truth.assertThat(resultMap).isEqualTo(expectedMap)
    }

    /**
     * test that groupBooksByTitle with empty
     * book list param
     * should return empty map
     **/
    @Test
    fun `test groupBooksByTitle with empty book list return empty map`() {
        val booksList = emptyList<Book>()

        val resultMap = groupBooksByTitle(booksList)

        val expectedMap = emptyMap<String, List<Book>>()
        Truth.assertThat(resultMap).isEqualTo(expectedMap)
    }

    /**
     * test that groupBooksByTitle
     * with book list param should return the correct
     * mapped books by title alphabet
     */
    @Test
    fun `test groupBooksByDatesInSameWeek with book list should return the correct mapped books by alphabet`() {
        val booksList = listOf(
            Book(
                "1",
                "book 1",
                "author 1",
                LocalDate.of(2018, 7, 3),
                "cover url"
            ),
            Book(
                "2",
                "aBook 2",
                "book 2",
                LocalDate.of(2018, 7, 2),
                "cover url"
            ),
            Book(
                "3",
                "book 3",
                "book 3",
                LocalDate.of(2018, 6, 19),
                "cover url"
            )
        )

        val resultMap = groupBooksByTitle(booksList)

        val expectedMap = mapOf(
            "B" to listOf(
                Book(
                    "1",
                    "book 1",
                    "author 1",
                    LocalDate.of(2018, 7, 3),
                    "cover url"
                ),
                Book(
                    "3",
                    "book 3",
                    "book 3",
                    LocalDate.of(2018, 6, 19),
                    "cover url"
                )
            ),
            "A" to listOf(
                Book(
                    "2",
                    "aBook 2",
                    "book 2",
                    LocalDate.of(2018, 7, 2),
                    "cover url"
                )
            )
        )

        Truth.assertThat(resultMap).isEqualTo(expectedMap)
    }

    /**
     * test that filterBooks
     * empty books list returns empty list
     * */
    @Test
    fun `test filterBooks with empty book list returns empty list`() {
        val booksList = emptyList<Book>()

        val resultList = filterBooks(
            isAscending = false,
            books = booksList,
            bookFilter = Constants.BookFilters.ALPHABET
        )

        val expectedList = emptyList<Book>()

        Truth.assertThat(resultList).isEqualTo(expectedList)
    }

    /**
     * test that filterBooks
     * book list with isAscending as true
     * and filter by Week returns correct list
     * */
    @Test
    fun `test filterBooks with isAscending true and filter by week returns correct list`() {
        val booksList = listOf(
            Book(
                "1",
                "book 1",
                "author 1",
                LocalDate.of(2018, 7, 3),
                "cover url"
            ),
            Book(
                "2",
                "aBook 2",
                "book 2",
                LocalDate.of(2018, 7, 2),
                "cover url"
            ),
            Book(
                "3",
                "book 3",
                "book 3",
                LocalDate.of(2018, 6, 19),
                "cover url"
            )
        )

        val resultList = filterBooks(
            isAscending = true,
            bookFilter = Constants.BookFilters.WEEK_OF_YEAR,
            books = booksList.shuffled()
        )

        val expectedList = listOf(
            Book(
                "3",
                "book 3",
                "book 3",
                LocalDate.of(2018, 6, 19),
                "cover url"
            ),
            Book(
                "2",
                "aBook 2",
                "book 2",
                LocalDate.of(2018, 7, 2),
                "cover url"
            ),
            Book(
                "1",
                "book 1",
                "author 1",
                LocalDate.of(2018, 7, 3),
                "cover url"
            )
        )

        Truth.assertThat(resultList).isEqualTo(expectedList)
    }

    /**
     * test that filterBooks
     * book list with isAscending as false
     * and filter by Week returns correct list
     * */
    @Test
    fun `test filterBooks isAscending false and filter by week returns correct list`() {
        val booksList = listOf(
            Book(
                "2",
                "aBook 2",
                "book 2",
                LocalDate.of(2018, 7, 2),
                "cover url"
            ),
            Book(
                "1",
                "book 1",
                "author 1",
                LocalDate.of(2018, 7, 3),
                "cover url"
            ),
            Book(
                "3",
                "book 3",
                "book 3",
                LocalDate.of(2018, 6, 19),
                "cover url"
            )
        )

        val resultList = filterBooks(
            isAscending = false,
            bookFilter = Constants.BookFilters.WEEK_OF_YEAR,
            books = booksList.shuffled()
        )

        val expectedList = listOf(
            Book(
                "1",
                "book 1",
                "author 1",
                LocalDate.of(2018, 7, 3),
                "cover url"
            ),
            Book(
                "2",
                "aBook 2",
                "book 2",
                LocalDate.of(2018, 7, 2),
                "cover url"
            ),
            Book(
                "3",
                "book 3",
                "book 3",
                LocalDate.of(2018, 6, 19),
                "cover url"
            )
        )

        Truth.assertThat(resultList).isEqualTo(expectedList)
    }

    /**
     * test that filterBooks
     * book list with isAscending as true
     * and filter by alphabet returns correct list
     * */
    @Test
    fun `test filterBooks with isAscending true and filter by alphabet returns correct list`() {
        val booksList = listOf(
            Book(
                "1",
                "book 1",
                "author 1",
                LocalDate.of(2018, 7, 3),
                "cover url"
            ),
            Book(
                "2",
                "aBook 2",
                "book 2",
                LocalDate.of(2018, 7, 2),
                "cover url"
            ),
            Book(
                "3",
                "book 3",
                "book 3",
                LocalDate.of(2018, 6, 19),
                "cover url"
            )
        )

        val resultList = filterBooks(
            isAscending = true,
            bookFilter = Constants.BookFilters.ALPHABET,
            books = booksList.shuffled()
        )

        val expectedList = listOf(
            Book(
                "2",
                "aBook 2",
                "book 2",
                LocalDate.of(2018, 7, 2),
                "cover url"
            ),
            Book(
                "1",
                "book 1",
                "author 1",
                LocalDate.of(2018, 7, 3),
                "cover url"
            ),
            Book(
                "3",
                "book 3",
                "book 3",
                LocalDate.of(2018, 6, 19),
                "cover url"
            )

        )

        Truth.assertThat(resultList).isEqualTo(expectedList)
    }

    /**
     * test that filterBooks
     * book list with isAscending as false
     * and filter by alphabet returns correct list
     * */
    @Test
    fun `test filterBooks with isAscending false and filter by alphabet returns correct list`() {
        val booksList = listOf(
            Book(
                "1",
                "book 1",
                "author 1",
                LocalDate.of(2018, 7, 3),
                "cover url"
            ),
            Book(
                "2",
                "aBook 2",
                "book 2",
                LocalDate.of(2018, 7, 2),
                "cover url"
            ),
            Book(
                "3",
                "book 3",
                "book 3",
                LocalDate.of(2018, 6, 19),
                "cover url"
            )
        )

        val resultList = filterBooks(
            isAscending = false,
            bookFilter = Constants.BookFilters.ALPHABET,
            books = booksList.shuffled()
        )

        val expectedList = listOf(
            Book(
                "3",
                "book 3",
                "book 3",
                LocalDate.of(2018, 6, 19),
                "cover url"
            ),
            Book(
                "1",
                "book 1",
                "author 1",
                LocalDate.of(2018, 7, 3),
                "cover url"
            ),
            Book(
                "2",
                "aBook 2",
                "book 2",
                LocalDate.of(2018, 7, 2),
                "cover url"
            )

        )

        Truth.assertThat(resultList).isEqualTo(expectedList)
    }

    /**
     * test LocalDate.toDateString()
     * it returns correct value yyyy-MM-dd
     * */
    @Test
    fun `test LocalDate toDateString() local date  returns the correct value`() {
        val localDate = LocalDate.of(2019, 9, 9)

        val result = localDate.toDateString()

        val expected = "2019-09-09"

        Truth.assertThat(result).isEqualTo(expected)
    }

    /**
     * test LocalDate.toDateString()
     *  returns incorrect value yyyy-MM-dd
     * */
    @Test
    fun `test LocalDate toDateString() local date  returns incorrect value`() {
        val localDate = LocalDate.of(2019, 9, 9)

        val result = localDate.toDateString()

        val expected = "2019-09-03"

        Truth.assertThat(result).isNotEqualTo(expected)
    }

    /**
     * test date yyyy-MM-dd String.toLocaleDate()
     * returns correct value
     * */
    @Test
    fun `test date String toLocaleDate() returns correct value`() {
        val date = "2019-09-03"

        val result = date.toLocaleDate()

        val expected = LocalDate.of(2019, 9, 3)

        Truth.assertThat(result).isEqualTo(expected)
    }

    /**
     * test date yyyy-MM-dd String.toLocaleDate()
     * returns correct value
     * */
    @Test
    fun `test date String toLocaleDate() returns incorrect value`() {
        val date = "2019-09-03"

        val result = date.toLocaleDate()

        val expected = LocalDate.of(2019, 9, 10)

        Truth.assertThat(result).isNotEqualTo(expected)
    }
}
