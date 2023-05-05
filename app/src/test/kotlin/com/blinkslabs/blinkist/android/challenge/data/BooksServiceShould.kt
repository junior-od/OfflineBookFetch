package com.blinkslabs.blinkist.android.challenge.data

import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.runner.RunWith
import org.mockito.junit.MockitoJUnitRunner

@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(MockitoJUnitRunner::class)
class BooksServiceShould {
//    @Mock
//    lateinit var booksApi: BooksApi
//
//    @InjectMocks
//    lateinit var booksService: BooksService
//
//    private val mockBooks: List<Book> = listOf(mock(), mock(), mock())
//
//    @Test
//    fun callBooksApiWhenGetBooksIsCalled() = runTest {
//        givenASuccessfulBooksApiCall(mockBooks)
//        booksService.getBooks()
//        verify(booksApi).getAllBooks()
//    }
//
//    @Test
//    fun returnBooksApiOutputWhenGetBooksIsSuccessful() = runTest {
//        givenASuccessfulBooksApiCall(mockBooks)
//        assert(booksService.getBooks() == mockBooks)
//    }
//
//    @Test(expected = RuntimeException::class)
//    fun propagateExceptionWhenGetBooksIsUnsuccesful() = runTest {
//        givenAnUnsuccessfulBooksApiCall(RuntimeException("test"))
//        booksService.getBooks()
//    }
//
//    private fun givenASuccessfulBooksApiCall(result: List<Book>) {
//        runBlocking { whenever(booksApi.getAllBooks()).thenReturn(result) }
//    }
//
//    private fun givenAnUnsuccessfulBooksApiCall(exception: Throwable) {
//        runBlocking { whenever(booksApi.getAllBooks()).thenThrow(exception) }
//    }
}
