package com.blinkslabs.blinkist.android.challenge.data.api

import com.blinkslabs.blinkist.android.challenge.data.model.Book

interface BooksApi {

    suspend fun getAllBooks(): List<Book>
}
