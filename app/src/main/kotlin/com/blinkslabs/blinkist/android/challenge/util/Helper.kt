package com.blinkslabs.blinkist.android.challenge.util

import com.blinkslabs.blinkist.android.challenge.data.model.Book
import org.threeten.bp.temporal.WeekFields
import java.util.Locale

object Helper {
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
}
