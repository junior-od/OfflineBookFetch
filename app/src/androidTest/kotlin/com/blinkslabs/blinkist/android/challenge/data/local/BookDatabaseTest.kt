package com.blinkslabs.blinkist.android.challenge.data.local

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.asLiveData
import androidx.room.Room
import androidx.test.filters.SmallTest
import androidx.test.platform.app.InstrumentationRegistry
import com.blinkslabs.blinkist.android.challenge.getOrAwaitValue
import com.google.common.truth.Truth
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
@SmallTest
class BookDatabaseTest {

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var bookDatabase: BookDatabase
    private lateinit var booksDao: BooksDao

    @Before
    fun setUp() {
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext

        bookDatabase = Room.inMemoryDatabaseBuilder(
            appContext,
            BookDatabase::class.java
        ).allowMainThreadQueries().build()

        booksDao = bookDatabase.dao
    }

    @After
    fun teardown() {
        bookDatabase.close()
    }

    /**
     * ensure the insertions of book list
     * or updating of values
     * via primary key,
     * check the list exists in db
     * returns true
     * */
    @Test
    fun testUpsertAllBooks_Returns_True() = runTest {
        val booksList = listOf(
            BookEntity(
                id = "1",
                name = "book 1",
                author = "author",
                publishDate = "2019-02-20",
                coverImageUrl = "urrl test",
                dateCreated = "2022-01-12"
            ),
            BookEntity(
                id = "2",
                name = "book 2",
                author = "autho 2",
                publishDate = "20188-02-22",
                coverImageUrl = "urrl test",
                dateCreated = "2022-01-12"
            )
        )

        booksDao.upsertAllBooks(booksList)

        val getBooks = booksDao.getAllBooks().asLiveData().getOrAwaitValue()

        Truth.assertThat(getBooks).isEqualTo(booksList)
    }

    /**
     * get one book from
     * the db if db is empty returns null
     */
    @Test
    fun testGetAnyBookIsNull_Returns_True() = runTest {
        val getBook = booksDao.getAnyBook()

        Truth.assertThat(getBook).isNull()
    }

    /**
     * get one book from
     *  db when not empty returns a book entity
     */
    @Test
    fun testGetAnyBookIsFoundInDb_Returns_True() = runTest {
        val booksList = listOf(
            BookEntity(
                id = "1",
                name = "book 1",
                author = "author",
                publishDate = "2019-02-20",
                coverImageUrl = "urrl test",
                dateCreated = "2022-01-12"
            ),
            BookEntity(
                id = "2",
                name = "book 2",
                author = "autho 2",
                publishDate = "20188-02-22",
                coverImageUrl = "urrl test",
                dateCreated = "2022-01-12"
            )
        )

        booksDao.upsertAllBooks(booksList)

        val getBook = booksDao.getAnyBook()

        Truth.assertThat(booksList).contains(getBook)
    }

    /**
     * get all books from
     *  db when not empty returns true
     */
    @Test
    fun testGetAllBooksWhenDbIsNotEmpty_Returns_True() = runTest {
        val booksList = listOf(
            BookEntity(
                id = "1",
                name = "book 1",
                author = "author",
                publishDate = "2019-02-20",
                coverImageUrl = "urrl test",
                dateCreated = "2022-01-12"
            ),
            BookEntity(
                id = "2",
                name = "book 2",
                author = "autho 2",
                publishDate = "20188-02-22",
                coverImageUrl = "urrl test",
                dateCreated = "2022-01-12"
            )
        )

        booksDao.upsertAllBooks(booksList)

        val getBooks = booksDao.getAllBooks().asLiveData().getOrAwaitValue()

        Truth.assertThat(getBooks).isNotEmpty()
    }

    /**
     * get all books from
     *  db when empty returns true
     */
    @Test
    fun testGetAllBooksWhenDbIsEmpty_Returns_True() = runTest {
        val getBooks = booksDao.getAllBooks().asLiveData().getOrAwaitValue()

        Truth.assertThat(getBooks).isEmpty()
    }

    /**
     * delete all books from
     *  db returns true
     */
    @Test
    fun testClearAllBooks_Returns_True() = runTest {
        val booksList = listOf(
            BookEntity(
                id = "1",
                name = "book 1",
                author = "author",
                publishDate = "2019-02-20",
                coverImageUrl = "urrl test",
                dateCreated = "2022-01-12"
            ),
            BookEntity(
                id = "2",
                name = "book 2",
                author = "autho 2",
                publishDate = "20188-02-22",
                coverImageUrl = "urrl test",
                dateCreated = "2022-01-12"
            )
        )

        booksDao.upsertAllBooks(booksList)

        booksDao.clearAll()

        val getBooks = booksDao.getAllBooks().asLiveData().getOrAwaitValue()

        Truth.assertThat(getBooks).containsNoneIn(booksList)
    }
}
