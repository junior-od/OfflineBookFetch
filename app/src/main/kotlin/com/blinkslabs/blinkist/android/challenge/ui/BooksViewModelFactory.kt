package com.blinkslabs.blinkist.android.challenge.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.blinkslabs.blinkist.android.challenge.data.repository.BooksRepoImpl
import javax.inject.Inject

class BooksViewModelFactory @Inject constructor(
    private val booksService: BooksRepoImpl
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T = BooksViewModel(booksService) as T
}
