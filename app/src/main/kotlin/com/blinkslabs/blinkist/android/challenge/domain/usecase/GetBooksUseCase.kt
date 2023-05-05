package com.blinkslabs.blinkist.android.challenge.domain.usecase

import com.blinkslabs.blinkist.android.challenge.data.model.Book
import com.blinkslabs.blinkist.android.challenge.data.repository.BooksRepo
import com.blinkslabs.blinkist.android.challenge.util.Helper
import javax.inject.Inject

class GetBooksUseCase @Inject constructor(private val booksRepo: BooksRepo) {

    suspend operator fun invoke(): Map<String, List<Book>> {
        return Helper.groupBooksByDatesInSameWeek(booksRepo.getBooks())
    }
}
