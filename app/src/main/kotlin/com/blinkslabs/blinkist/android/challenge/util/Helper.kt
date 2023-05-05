package com.blinkslabs.blinkist.android.challenge.util

import com.blinkslabs.blinkist.android.challenge.data.model.Book
import org.threeten.bp.LocalDate
import org.threeten.bp.format.DateTimeFormatter
import org.threeten.bp.temporal.WeekFields
import java.util.Locale

fun groupBooksByDatesInSameWeek(booklist: List<Book>): Map<String, List<Book>> {
    val groupedBooksByDates = mutableMapOf<String, MutableList<Book>>()

    for (book in booklist) {
        val getDateWeekofYear = book.publishDate.get(WeekFields.of(Locale.getDefault()).weekOfYear())

        val getDateWeekofYearWithActualYear = "$getDateWeekofYear ${book.publishDate.year}"

        if (groupedBooksByDates.containsKey(getDateWeekofYearWithActualYear)) {
            groupedBooksByDates[getDateWeekofYearWithActualYear]?.add(book)
        } else {
            groupedBooksByDates[getDateWeekofYearWithActualYear] = mutableListOf(book)
        }
    }

    return groupedBooksByDates
}

fun groupBooksByTitle(booklist: List<Book>): Map<String, List<Book>> {
    val groupedBooks = mutableMapOf<String, MutableList<Book>>()

    for (book in booklist) {
        val getFirstLetterFromTitle = book.name[0].toString().uppercase()

        if (groupedBooks.containsKey(getFirstLetterFromTitle)) {
            groupedBooks[getFirstLetterFromTitle]?.add(book)
        } else {
            groupedBooks[getFirstLetterFromTitle] = mutableListOf(book)
        }
    }

    return groupedBooks
}

fun filterBooks(isAscending: Boolean, books: List<Book>, bookFilter: Constants.BookFilters): List<Book> {
    return when (bookFilter) {
        Constants.BookFilters.ALPHABET -> {
            if (isAscending) books.sortedBy { it.name } else books.sortedByDescending { it.name }
        }
        Constants.BookFilters.WEEK_OF_YEAR -> {
            if (isAscending) books.sortedBy { it.publishDate } else books.sortedByDescending { it.publishDate }
        }
    }
}

fun LocalDate.toDateString(): String {
    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
    return this.format(formatter)
}

fun String.toLocaleDate(): LocalDate {
    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
    val regex = "\\d{4}-\\d{2}-\\d{2}" // This regex matches the yyyy-MM-dd pattern
    val hasDatePattern = regex.toRegex().matches(this)

    return if (hasDatePattern) {

        LocalDate.parse(this, formatter)

    } else {
        LocalDate.of(1900,10,10)
    }



}
