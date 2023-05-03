package com.blinkslabs.blinkist.android.challenge.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.blinkslabs.blinkist.android.challenge.data.BooksService
import com.blinkslabs.blinkist.android.challenge.data.model.Book
import io.reactivex.disposables.CompositeDisposable
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

class BooksViewModel @Inject constructor(private val booksService: BooksService) : ViewModel() {

    private val subscriptions = CompositeDisposable()

    private val books = MutableLiveData<List<Book>>()

    fun books(): LiveData<List<Book>> = books

    fun fetchBooks() {
        viewModelScope.launch {
            try {
                books.value = booksService.getBooks()
            } catch (e: Exception) {
                Timber.e(e, "while loading data")
            }

        }
    }

    override fun onCleared() {
        subscriptions.clear()
    }
}
