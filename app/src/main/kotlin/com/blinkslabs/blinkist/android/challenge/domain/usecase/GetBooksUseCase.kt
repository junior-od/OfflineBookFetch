package com.blinkslabs.blinkist.android.challenge.domain.usecase

import com.blinkslabs.blinkist.android.challenge.data.model.Book
import com.blinkslabs.blinkist.android.challenge.data.repository.BooksRepo
import com.blinkslabs.blinkist.android.challenge.util.Constants
import com.blinkslabs.blinkist.android.challenge.util.filterBooks
import com.blinkslabs.blinkist.android.challenge.util.groupBooksByDatesInSameWeek
import com.blinkslabs.blinkist.android.challenge.util.groupBooksByTitle
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GetBooksUseCase @Inject constructor(
    private val booksRepo: BooksRepo

) {
    operator fun invoke(
        isAscending: Boolean = false,
        bookFilter: Constants.BookFilters
    ): Flow<Map<String, List<Book>>> {
        return booksRepo.getBooks().map { books ->

            val filteredBooks = filterBooks(
                isAscending,
                books,
                bookFilter
            )

            when (bookFilter) {
                Constants.BookFilters.WEEK_OF_YEAR -> {
                    groupBooksByDatesInSameWeek(
                        filteredBooks
                    )
                }

                Constants.BookFilters.ALPHABET -> {
                    groupBooksByTitle(
                        filteredBooks
                    )
                }
            }
        }
    }
}
