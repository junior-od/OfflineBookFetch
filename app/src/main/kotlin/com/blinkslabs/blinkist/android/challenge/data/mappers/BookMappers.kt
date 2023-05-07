package com.blinkslabs.blinkist.android.challenge.data.mappers

import com.blinkslabs.blinkist.android.challenge.data.local.BookEntity
import com.blinkslabs.blinkist.android.challenge.data.model.Book
import com.blinkslabs.blinkist.android.challenge.util.toDateString
import com.blinkslabs.blinkist.android.challenge.util.toLocaleDate
import org.threeten.bp.LocalDate

fun Book.toBookEntity(): BookEntity {
    return BookEntity(
        id = id,
        name = name,
        author = author,
        publishDate = publishDate.toDateString(),
        coverImageUrl = coverImageUrl,
        dateCreated = LocalDate.now().toDateString()
    )
}

fun BookEntity.toBook(): Book {
    return Book(
        id = id,
        name = name,
        author = author,
        publishDate = publishDate.toLocaleDate(),
        coverImageUrl = coverImageUrl
    )
}
