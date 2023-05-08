package com.blinkslabs.blinkist.android.challenge.data.repository

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.asLiveData
import androidx.room.Room
import androidx.test.platform.app.InstrumentationRegistry
import com.blinkslabs.blinkist.android.challenge.data.api.BooksApi
import com.blinkslabs.blinkist.android.challenge.data.local.BookDatabase
import com.blinkslabs.blinkist.android.challenge.data.local.BookEntity
import com.blinkslabs.blinkist.android.challenge.data.local.BooksDao
import com.blinkslabs.blinkist.android.challenge.data.mappers.toBook
import com.blinkslabs.blinkist.android.challenge.data.model.Book
import com.blinkslabs.blinkist.android.challenge.getOrAwaitValue
import com.blinkslabs.blinkist.android.challenge.util.toDateString
import com.google.common.truth.Truth
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito.mock
import org.mockito.Mockito.`when`
import org.threeten.bp.LocalDate

@OptIn(ExperimentalCoroutinesApi::class)
class BooksRepoImplTest {

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var bookDatabase: BookDatabase
    private lateinit var booksDao: BooksDao

    private lateinit var booksRepoImpl: BooksRepo

    private lateinit var booksApi: BooksApi

    @Before
    fun setUp() {
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext

        bookDatabase = Room.inMemoryDatabaseBuilder(
            appContext,
            BookDatabase::class.java
        ).allowMainThreadQueries().build()

        booksDao = bookDatabase.dao

        booksApi = mock(BooksApi::class.java)
    }

    @After
    fun teardown() {
        bookDatabase.close()
    }

    /**
     * test fetchbooks on first time and when
     * api response has empty list data
     */
    @Test
    fun testFetchBooksOnFirstTimeRequestWhenResponseIsEmpty_returns() = runTest {
        `when`(booksApi.getAllBooks()).thenReturn(emptyList())

        booksRepoImpl = BooksRepoImpl(
            booksDatabase = bookDatabase,
            booksApi = booksApi
        )

        booksRepoImpl.fetchBooks()

        val getBooks = booksDao.getAllBooks().asLiveData().getOrAwaitValue()

        Truth.assertThat(getBooks).isEmpty()
    }

    /**
     * test fetchbooks on first time and when
     * api response has list data
     */
    @Test
    fun testFetchBooksOnFirstTimeRequestWhenResponseIsNotEmpty_returns() = runTest {
        `when`(booksApi.getAllBooks()).thenReturn(
            listOf(
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
        )

        booksRepoImpl = BooksRepoImpl(
            booksDatabase = bookDatabase,
            booksApi = booksApi
        )

        booksRepoImpl.fetchBooks()

        val getBooks = booksDao.getAllBooks().asLiveData().getOrAwaitValue()

        Truth.assertThat(getBooks).isNotEmpty()
    }

    /**
     * test fetchbooks when checkUpdates is true and when
     * api response has list data
     */
    @Test
    fun testFetchBooks_SetCheckUpdatesTrue_WhenResponseIsNotEmpty_returns() = runTest {
        `when`(booksApi.getAllBooks()).thenReturn(
            listOf(
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
        )

        booksRepoImpl = BooksRepoImpl(
            booksDatabase = bookDatabase,
            booksApi = booksApi
        )

        booksRepoImpl.fetchBooks(
            checkUpdates = true
        )

        val getBooks = booksDao.getAllBooks().asLiveData().getOrAwaitValue()

        Truth.assertThat(getBooks).isNotEmpty()
    }

    /**
     * test fetchbooks when db data date created is before today
     * and when api response has list data
     * should trigger api call
     */
    @Test
    fun testFetchBooks_WhenDbDataDateCreatedIsBeforeToday_WhenResponseIsNotEmpty_returns() = runTest {
        val booksList = listOf(
            BookEntity(
                id = "1",
                name = "book 1",
                author = "author",
                publishDate = "2019-02-20",
                coverImageUrl = "urrl test",
                dateCreated = LocalDate.now().minusDays(1).toDateString()
            ),
            BookEntity(
                id = "2",
                name = "book 2",
                author = "autho 2",
                publishDate = "20188-02-22",
                coverImageUrl = "urrl test",
                dateCreated = LocalDate.now().minusDays(1).toDateString()
            )
        )

        booksDao.upsertAllBooks(booksList)

        val apiUpdatedBooks = listOf(
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

        `when`(booksApi.getAllBooks()).thenReturn(apiUpdatedBooks)

        booksRepoImpl = BooksRepoImpl(
            booksDatabase = bookDatabase,
            booksApi = booksApi
        )

        booksRepoImpl.fetchBooks(
            checkUpdates = false
        )

        val getBooks = booksDao.getAllBooks().asLiveData().getOrAwaitValue()

        Truth.assertThat(getBooks.map { it.toBook() }).containsExactlyElementsIn(apiUpdatedBooks)
    }

    /**
     * test fetchbooks when db data date created is today
     * and when api response has list data
     * should not trigger api call and return previous db data
     */
    @Test
    fun testFetchBooks_WhenDbDataDateCreatedIsToday_WhenResponseIsNotEmpty_returns() = runTest {
        val booksList = listOf(
            BookEntity(
                id = "1",
                name = "book 1",
                author = "author",
                publishDate = "2019-02-20",
                coverImageUrl = "urrl test",
                dateCreated = LocalDate.now().toDateString()
            ),
            BookEntity(
                id = "2",
                name = "book 2",
                author = "autho 2",
                publishDate = "20188-02-22",
                coverImageUrl = "urrl test",
                dateCreated = LocalDate.now().toDateString()
            )
        )

        booksDao.upsertAllBooks(booksList)

        val apiUpdatedBooks = listOf(
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

        `when`(booksApi.getAllBooks()).thenReturn(apiUpdatedBooks)

        booksRepoImpl = BooksRepoImpl(
            booksDatabase = bookDatabase,
            booksApi = booksApi
        )

        booksRepoImpl.fetchBooks(
            checkUpdates = false
        )

        val getBooks = booksDao.getAllBooks().asLiveData().getOrAwaitValue()

        Truth.assertThat(getBooks).containsExactlyElementsIn(booksList)
    }

    /**
     * test fetchbooks when db data date created is future date
     * and when api response has list data
     * should not trigger api call and return previous db data
     */
    @Test
    fun testFetchBooks_WhenDbDataDateCreatedIsFutureDate_WhenResponseIsNotEmpty_returns() = runTest {
        val booksList = listOf(
            BookEntity(
                id = "1",
                name = "book 1",
                author = "author",
                publishDate = "2019-02-20",
                coverImageUrl = "urrl test",
                dateCreated = LocalDate.now().plusDays(1).toDateString()
            ),
            BookEntity(
                id = "2",
                name = "book 2",
                author = "autho 2",
                publishDate = "20188-02-22",
                coverImageUrl = "urrl test",
                dateCreated = LocalDate.now().plusDays(1).toDateString()
            )
        )

        booksDao.upsertAllBooks(booksList)

        val apiUpdatedBooks = listOf(
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

        `when`(booksApi.getAllBooks()).thenReturn(apiUpdatedBooks)

        booksRepoImpl = BooksRepoImpl(
            booksDatabase = bookDatabase,
            booksApi = booksApi
        )

        booksRepoImpl.fetchBooks(
            checkUpdates = false
        )

        val getBooks = booksDao.getAllBooks().asLiveData().getOrAwaitValue()

        Truth.assertThat(getBooks).containsExactlyElementsIn(booksList)
    }

    /**
     * test getAllBooks from db
     * */
    @Test
    fun testGetAllBooksFromDb() = runTest {
        val booksList = listOf(
            BookEntity(
                id = "1",
                name = "book 1",
                author = "author",
                publishDate = "2019-02-20",
                coverImageUrl = "urrl test",
                dateCreated = LocalDate.now().plusDays(1).toDateString()
            ),
            BookEntity(
                id = "2",
                name = "book 2",
                author = "autho 2",
                publishDate = "20188-02-22",
                coverImageUrl = "urrl test",
                dateCreated = LocalDate.now().plusDays(1).toDateString()
            )
        )

        booksDao.upsertAllBooks(booksList)

        booksRepoImpl = BooksRepoImpl(
            booksDatabase = bookDatabase,
            booksApi = booksApi
        )
        val getBooks = booksRepoImpl.getBooks().asLiveData().getOrAwaitValue()

        val expected = booksList.map { it.toBook() }

        Truth.assertThat(getBooks).containsExactlyElementsIn(expected)
    }

    /**
     * test clearAllBooks from db
     * */
    @Test
    fun testClearAllBooksFromDb() = runTest {
        val booksList = listOf(
            BookEntity(
                id = "1",
                name = "book 1",
                author = "author",
                publishDate = "2019-02-20",
                coverImageUrl = "urrl test",
                dateCreated = LocalDate.now().plusDays(1).toDateString()
            ),
            BookEntity(
                id = "2",
                name = "book 2",
                author = "autho 2",
                publishDate = "20188-02-22",
                coverImageUrl = "urrl test",
                dateCreated = LocalDate.now().plusDays(1).toDateString()
            )
        )

        booksDao.upsertAllBooks(booksList)

        booksRepoImpl = BooksRepoImpl(
            booksDatabase = bookDatabase,
            booksApi = booksApi
        )

        booksRepoImpl.clearBooks()

        val getBooks = booksRepoImpl.getBooks().asLiveData().getOrAwaitValue()

        Truth.assertThat(getBooks).isEmpty()
    }
}
