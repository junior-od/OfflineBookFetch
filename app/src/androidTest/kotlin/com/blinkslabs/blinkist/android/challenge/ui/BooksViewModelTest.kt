package com.blinkslabs.blinkist.android.challenge.ui

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.asLiveData
import androidx.room.Room
import androidx.test.platform.app.InstrumentationRegistry
import com.blinkslabs.blinkist.android.challenge.CoroutinesTestRule
import com.blinkslabs.blinkist.android.challenge.data.api.BooksApi
import com.blinkslabs.blinkist.android.challenge.data.local.BookDatabase
import com.blinkslabs.blinkist.android.challenge.data.local.BookEntity
import com.blinkslabs.blinkist.android.challenge.data.local.BooksDao
import com.blinkslabs.blinkist.android.challenge.data.model.Book
import com.blinkslabs.blinkist.android.challenge.data.repository.BooksRepo
import com.blinkslabs.blinkist.android.challenge.data.repository.BooksRepoImpl
import com.blinkslabs.blinkist.android.challenge.domain.usecase.GetBooksUseCase
import com.blinkslabs.blinkist.android.challenge.getOrAwaitValue
import com.blinkslabs.blinkist.android.challenge.util.Constants
import com.blinkslabs.blinkist.android.challenge.util.toDateString
import com.google.common.truth.Truth
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito
import org.mockito.Mockito.mock
import org.threeten.bp.LocalDate

@OptIn(ExperimentalCoroutinesApi::class)
class BooksViewModelTest {
    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val coroutinesRule = CoroutinesTestRule()

    private lateinit var bookDatabase: BookDatabase
    private lateinit var booksDao: BooksDao

    private lateinit var booksRepoImpl: BooksRepo

    private lateinit var booksApi: BooksApi

    private lateinit var getBooksUseCase: GetBooksUseCase

    private lateinit var booksViewModel: BooksViewModel

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

    @Test
    fun testThatBooksRepoFetchBooksIsCalledSuccessfullyAndResultIsNotEmpty() = runTest {
        Mockito.`when`(booksApi.getAllBooks()).thenReturn(
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
            booksApi = booksApi,
            booksDatabase = bookDatabase
        )
        getBooksUseCase = GetBooksUseCase(booksRepoImpl)
        booksViewModel = BooksViewModel(
            getBooksUseCase,
            booksRepoImpl
        )

        val result = booksViewModel.books.asLiveData().getOrAwaitValue()

        Truth.assertThat(result.groupedBooksList).isNotEmpty()
        Truth.assertThat(result.isLoading).isFalse()
        Truth.assertThat(result.isRefreshing).isFalse()
    }

    @Test
    fun testThatBooksRepoFetchBooksIsCalledSuccessfullyAndResultIsEmpty() = runTest {
        Mockito.`when`(booksApi.getAllBooks()).thenReturn(
            emptyList()
        )

        booksRepoImpl = BooksRepoImpl(
            booksApi = booksApi,
            booksDatabase = bookDatabase
        )
        getBooksUseCase = GetBooksUseCase(booksRepoImpl)
        booksViewModel = BooksViewModel(
            getBooksUseCase,
            booksRepoImpl
        )

        val result = booksViewModel.books.asLiveData().getOrAwaitValue()

        Truth.assertThat(result.groupedBooksList).isEmpty()
    }

    @Test
    fun testThatViewModelLoadsDataFromDb() = runTest {
        val booksList = listOf(
            BookEntity(
                "1",
                "book 1",
                "author 1",
                "2018-07-03",
                "cover url",
                LocalDate.now().plusDays(1).toDateString()
            ),
            BookEntity(
                "2",
                "book 2",
                "book 2",
                "2018-07-02",
                "cover url",
                LocalDate.now().plusDays(1).toDateString()
            ),
            BookEntity(
                "3",
                "book 3",
                "book 3",
                "2018-06-19",
                "cover url",
                LocalDate.now().plusDays(1).toDateString()
            )
        )
        bookDatabase.dao.upsertAllBooks(booksList = booksList)

        booksRepoImpl = BooksRepoImpl(
            booksApi = booksApi,
            booksDatabase = bookDatabase
        )
        getBooksUseCase = GetBooksUseCase(booksRepoImpl)
        booksViewModel = BooksViewModel(
            getBooksUseCase,
            booksRepoImpl
        )

        val result = booksViewModel.books.asLiveData().getOrAwaitValue()

        Truth.assertThat(result.groupedBooksList).isNotEmpty()
        Truth.assertThat(result.isLoading).isFalse()
        Truth.assertThat(result.isRefreshing).isFalse()
    }

    @Test
    fun testThatFilterBookStateUpdates() = runTest {
        booksRepoImpl = BooksRepoImpl(
            booksApi = booksApi,
            booksDatabase = bookDatabase
        )
        getBooksUseCase = GetBooksUseCase(booksRepoImpl)
        booksViewModel = BooksViewModel(
            getBooksUseCase,
            booksRepoImpl
        )

        booksViewModel.filterBooks(isAscending = true, bookFilter = Constants.BookFilters.ALPHABET)

        val result = booksViewModel.filterBook.asLiveData().getOrAwaitValue()

        Truth.assertThat(result.isAscending).isTrue()
        Truth.assertThat(result.bookFilter).isEqualTo(Constants.BookFilters.ALPHABET)
    }
}
