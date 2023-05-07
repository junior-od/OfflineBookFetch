package com.blinkslabs.blinkist.android.challenge.data.repository

import androidx.room.withTransaction
import com.blinkslabs.blinkist.android.challenge.data.api.BooksApi
import com.blinkslabs.blinkist.android.challenge.data.local.BookDatabase
import com.blinkslabs.blinkist.android.challenge.data.mappers.toBook
import com.blinkslabs.blinkist.android.challenge.data.mappers.toBookEntity
import com.blinkslabs.blinkist.android.challenge.data.model.Book
import com.blinkslabs.blinkist.android.challenge.util.shouldGetTodaysUpdate
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import timber.log.Timber
import javax.inject.Inject

class BooksRepoImpl @Inject constructor(
    private val booksApi: BooksApi,
    private val booksDatabase: BookDatabase
) : BooksRepo {

    override suspend fun fetchBooks(checkUpdates: Boolean) {
        Timber.tag("yeah").d("fetch book called")

        booksDatabase.withTransaction {
            val anyBook = booksDatabase.dao.getAnyBook()
            val noCache = anyBook == null

            val shouldGetTodaysUpdate = anyBook.shouldGetTodaysUpdate()

            Timber.tag("yeah").d(noCache.toString())

            Timber.tag("yeah").d("should fetch today update : $shouldGetTodaysUpdate")

            if (checkUpdates || noCache || shouldGetTodaysUpdate) {
                Timber.tag("yeah").d("api call made")
                // get from api
                val bookList = booksApi.getAllBooks()

                booksDatabase.dao.clearAll()

                val bookListEntities = bookList.map { it.toBookEntity() }

                booksDatabase.dao.upsertAllBooks(bookListEntities)
            }
        }
    }

    override fun getBooks(): Flow<List<Book>> {
        val booksList = booksDatabase.dao.getAllBooks()

        return booksList.map { it.map { bookEntity -> bookEntity.toBook() } }
    }

    override suspend fun clearBooks() = booksDatabase.dao.clearAll()
}
