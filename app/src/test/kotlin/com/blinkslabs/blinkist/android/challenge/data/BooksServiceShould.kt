package com.blinkslabs.blinkist.android.challenge.data

import com.blinkslabs.blinkist.android.challenge.data.api.BooksApi
import com.blinkslabs.blinkist.android.challenge.data.model.Book
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runTest
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner

@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(MockitoJUnitRunner::class)
class BooksServiceShould {
    @Mock
    lateinit var booksApi: BooksApi

    @InjectMocks
    lateinit var booksService: BooksService

    private val mockBooks: List<Book> = listOf(mock(), mock(), mock())

    @Test
    fun callBooksApiWhenGetBooksIsCalled() = runTest {
        givenASuccessfulBooksApiCall(mockBooks)
        booksService.getBooks()
        verify(booksApi).getAllBooks()
    }

    @Test
    fun returnBooksApiOutputWhenGetBooksIsSuccessful() = runTest {
        givenASuccessfulBooksApiCall(mockBooks)
        assert(booksService.getBooks() == mockBooks)
    }

    @Test(expected = RuntimeException::class)
    fun propagateExceptionWhenGetBooksIsUnsuccesful() = runTest {
        givenAnUnsuccessfulBooksApiCall(RuntimeException("test"))
        booksService.getBooks()
    }

    private fun givenASuccessfulBooksApiCall(result: List<Book>) {
        runBlocking { whenever(booksApi.getAllBooks()).thenReturn(result) }
    }

    private fun givenAnUnsuccessfulBooksApiCall(exception: Throwable) {
        runBlocking { whenever(booksApi.getAllBooks()).thenThrow(exception) }
    }
}
