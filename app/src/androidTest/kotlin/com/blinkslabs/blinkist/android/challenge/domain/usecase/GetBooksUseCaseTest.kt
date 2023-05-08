package com.blinkslabs.blinkist.android.challenge.domain.usecase

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.asLiveData
import androidx.room.Room
import androidx.test.platform.app.InstrumentationRegistry
import com.blinkslabs.blinkist.android.challenge.data.api.BooksApi
import com.blinkslabs.blinkist.android.challenge.data.local.BookDatabase
import com.blinkslabs.blinkist.android.challenge.data.local.BookEntity
import com.blinkslabs.blinkist.android.challenge.data.local.BooksDao
import com.blinkslabs.blinkist.android.challenge.data.model.Book
import com.blinkslabs.blinkist.android.challenge.data.repository.BooksRepo
import com.blinkslabs.blinkist.android.challenge.data.repository.BooksRepoImpl
import com.blinkslabs.blinkist.android.challenge.getOrAwaitValue
import com.blinkslabs.blinkist.android.challenge.util.Constants
import com.google.common.truth.Truth
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito
import org.threeten.bp.LocalDate

@OptIn(ExperimentalCoroutinesApi::class)
class GetBooksUseCaseTest {

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var bookDatabase: BookDatabase
    private lateinit var booksDao: BooksDao

    private lateinit var booksRepoImpl: BooksRepo

    private lateinit var booksApi: BooksApi

    private lateinit var getBooksUseCase: GetBooksUseCase

    @Before
    fun setUp() {
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext

        bookDatabase = Room.inMemoryDatabaseBuilder(
            appContext,
            BookDatabase::class.java
        ).allowMainThreadQueries().build()

        booksDao = bookDatabase.dao

        booksApi = Mockito.mock(BooksApi::class.java)

        booksRepoImpl = BooksRepoImpl(
            booksApi = booksApi,
            booksDatabase = bookDatabase
        )

        getBooksUseCase = GetBooksUseCase(booksRepoImpl)
    }

    @After
    fun teardown() {
        bookDatabase.close()
    }

    /**
     * test that GetBooksUsecase.invoke()
     * when empty books list returns empty map
     * */
    @Test
    fun testGetBooksUseCaseInvokeWhenEmptyBooksList_Returns_emptyList() = runTest {
        val getBooks = getBooksUseCase.invoke(
            isAscending = false,
            bookFilter = Constants.BookFilters.WEEK_OF_YEAR
        ).asLiveData().getOrAwaitValue()

        Truth.assertThat(getBooks).isEmpty()
    }

    /**
     * test that GetBooksUsecase.invoke()
     * with isAscending as true
     * and filter publish date by Week
     * when books list returns empty map
     * */
    @Test
    fun testGetBooksUseCaseInvokeWhenIsAscendingTrueAndByWeek_Returns() = runTest {
        val booksList = listOf(
            BookEntity(
                "1",
                "book 1",
                "author 1",
                "2018-07-03",
                "cover url",
                "2018-07-03"
            ),
            BookEntity(
                "2",
                "book 2",
                "book 2",
                "2018-07-02",
                "cover url",
                "2018-07-03"
            ),
            BookEntity(
                "3",
                "book 3",
                "book 3",
                "2018-06-19",
                "cover url",
                "2018-07-03"
            )
        )
        bookDatabase.dao.upsertAllBooks(booksList = booksList)

        val result = getBooksUseCase.invoke(
            isAscending = true,
            bookFilter = Constants.BookFilters.WEEK_OF_YEAR
        ).asLiveData().getOrAwaitValue()

        val expected = mapOf(
            "Week 25, 2018" to listOf(
                Book(
                    "3",
                    "book 3",
                    "book 3",
                    LocalDate.of(2018, 6, 19),
                    "cover url"
                )
            ),
            "Week 27, 2018" to listOf(
                Book(
                    "2",
                    "book 2",
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

        )

        Truth.assertThat(result).isEqualTo(expected)
    }

    /**
     * test that GetBooksUsecase.invoke()
     * with isAscending as false
     * and filter publish date by Week
     * when books list returns empty map
     * */
    @Test
    fun testGetBooksUseCaseInvokeWhenIsAscendingFalseAndByWeek_Returns() = runTest {
        val booksList = listOf(
            BookEntity(
                "1",
                "book 1",
                "author 1",
                "2018-07-03",
                "cover url",
                "2018-07-03"
            ),
            BookEntity(
                "2",
                "book 2",
                "book 2",
                "2018-07-02",
                "cover url",
                "2018-07-03"
            ),
            BookEntity(
                "3",
                "book 3",
                "book 3",
                "2018-06-19",
                "cover url",
                "2018-07-03"
            )
        )
        bookDatabase.dao.upsertAllBooks(booksList = booksList)

        val result = getBooksUseCase.invoke(
            isAscending = false,
            bookFilter = Constants.BookFilters.WEEK_OF_YEAR
        ).asLiveData().getOrAwaitValue()

        val expected = mapOf(
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

        Truth.assertThat(result).isEqualTo(expected)
    }

    /**
     * test that GetBooksUsecase.invoke()
     * with isAscending as true
     * and filter title by Alphabet
     * when books list returns empty map
     * */
    @Test
    fun testGetBooksUseCaseInvokeWhenIsAscendingTrueAndByAlphabet_Returns() = runTest {
        val booksList = listOf(
            BookEntity(
                "1",
                "book 1",
                "author 1",
                "2018-07-03",
                "cover url",
                "2018-07-03"
            ),
            BookEntity(
                "2",
                "aBook 2",
                "book 2",
                "2018-07-02",
                "cover url",
                "2018-07-03"
            ),
            BookEntity(
                "3",
                "book 3",
                "book 3",
                "2018-06-19",
                "cover url",
                "2018-07-03"
            )
        )
        bookDatabase.dao.upsertAllBooks(booksList = booksList)

        val result = getBooksUseCase.invoke(
            isAscending = true,
            bookFilter = Constants.BookFilters.ALPHABET
        ).asLiveData().getOrAwaitValue()

        val expected = mapOf(
            "A" to listOf(
                Book(
                    "2",
                    "aBook 2",
                    "book 2",
                    LocalDate.of(2018, 7, 2),
                    "cover url"
                )
            ),
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
            )
        )

        Truth.assertThat(result).isEqualTo(expected)
    }

    /**
     * test that GetBooksUsecase.invoke()
     * with isAscending as false
     * and filter title by Alphabet
     * when books list returns empty map
     * */
    @Test
    fun testGetBooksUseCaseInvokeWhenIsAscendingFalseAndByAlphabet_Returns() = runTest {
        val booksList = listOf(
            BookEntity(
                "1",
                "book 1",
                "author 1",
                "2018-07-03",
                "cover url",
                "2018-07-03"
            ),
            BookEntity(
                "2",
                "aBook 2",
                "book 2",
                "2018-07-02",
                "cover url",
                "2018-07-03"
            ),
            BookEntity(
                "3",
                "book 3",
                "book 3",
                "2018-06-19",
                "cover url",
                "2018-07-03"
            )
        )
        bookDatabase.dao.upsertAllBooks(booksList = booksList)

        val result = getBooksUseCase.invoke(
            isAscending = false,
            bookFilter = Constants.BookFilters.ALPHABET
        ).asLiveData().getOrAwaitValue()

        val expected = mapOf(
            "B" to listOf(
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

        Truth.assertThat(result).isEqualTo(expected)
    }
}
