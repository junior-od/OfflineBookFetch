package com.blinkslabs.blinkist.android.challenge.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.blinkslabs.blinkist.android.challenge.data.network.ConnectivityObserver
import com.blinkslabs.blinkist.android.challenge.data.repository.BooksRepo
import com.blinkslabs.blinkist.android.challenge.domain.usecase.GetBooksUseCase
import javax.inject.Inject

class BooksViewModelFactory @Inject constructor(
    private val getBooksUseCase: GetBooksUseCase,
    private val booksRepo: BooksRepo,
    private val connectivityObserver: ConnectivityObserver
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T = BooksViewModel(getBooksUseCase, booksRepo, connectivityObserver) as T
}
