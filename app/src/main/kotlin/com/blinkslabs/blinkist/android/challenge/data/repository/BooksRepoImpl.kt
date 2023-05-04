package com.blinkslabs.blinkist.android.challenge.data.repository

import com.blinkslabs.blinkist.android.challenge.data.api.BooksApi
import com.blinkslabs.blinkist.android.challenge.data.model.Book
import javax.inject.Inject

class BooksRepoImpl @Inject constructor(private val booksApi: BooksApi): BooksRepo {

    override suspend fun getBooks(): List<Book> = booksApi.getAllBooks()

}
