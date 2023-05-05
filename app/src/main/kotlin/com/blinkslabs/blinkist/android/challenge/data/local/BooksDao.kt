package com.blinkslabs.blinkist.android.challenge.data.local

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow

@Dao
interface BooksDao {

    @Upsert
    suspend fun upsertAllBooks(booksList: List<BookEntity>)

    @Query("SELECT * FROM BookEntity LIMIT 1")
    suspend fun getAnyBook(): BookEntity?

    @Query("SELECT * FROM BookEntity")
    fun getAllBooks(): Flow<List<BookEntity>>

    @Query("DELETE  FROM BookEntity")
    suspend fun clearAll()
}
