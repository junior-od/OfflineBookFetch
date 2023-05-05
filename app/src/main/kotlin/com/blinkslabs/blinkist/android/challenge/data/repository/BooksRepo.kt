package com.blinkslabs.blinkist.android.challenge.data.repository

import com.blinkslabs.blinkist.android.challenge.data.model.Book
import kotlinx.coroutines.flow.Flow

interface BooksRepo {

    suspend fun fetchBooks(
        checkUpdates: Boolean = false
    )

    fun getBooks(): Flow<List<Book>>

    suspend fun clearBooks()
}
