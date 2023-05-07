package com.blinkslabs.blinkist.android.challenge.util

object Constants {

    object TimePatterns {
        const val yyyyMMdd = "yyyy-MM-dd"
    }

    object RegexPatterns {
        const val isYyyyMmDdPattern = "\\d{4}-\\d{2}-\\d{2}"
    }

    enum class BookFilters {
        ALPHABET,
        WEEK_OF_YEAR,
    }
}
