package com.blinkslabs.blinkist.android.challenge.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class BookEntity(
    @PrimaryKey val id: String,
    val name: String,
    val author: String,
    val publishDate: String,
    val coverImageUrl: String,
    val dateCreated: String
)
