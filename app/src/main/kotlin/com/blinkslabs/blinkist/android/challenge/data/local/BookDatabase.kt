package com.blinkslabs.blinkist.android.challenge.data.local

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [BookEntity::class],
    version = 1
)
abstract class BookDatabase : RoomDatabase() {

    abstract val dao: BooksDao

    companion object {
        const val DATABASE_NAME = "books_db"
    }
}
