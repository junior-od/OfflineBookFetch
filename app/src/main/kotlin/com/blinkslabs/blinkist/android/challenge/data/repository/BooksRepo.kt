package com.blinkslabs.blinkist.android.challenge.data.repository

import com.blinkslabs.blinkist.android.challenge.data.model.Book

interface BooksRepo {

    suspend fun getBooks(): List<Book>
}