package com.blinkslabs.blinkist.android.challenge.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.blinkslabs.blinkist.android.challenge.data.model.Book
import com.blinkslabs.blinkist.android.challenge.domain.usecase.GetBooksUseCase
import io.reactivex.disposables.CompositeDisposable
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

class BooksViewModel @Inject constructor(private val getBooksUseCase: GetBooksUseCase) : ViewModel() {

    private val subscriptions = CompositeDisposable()

    private val _booksState = MutableStateFlow(GetBooksState())

    val books get() = _booksState.asStateFlow()

    init {
        fetchBooks()
    }

    fun fetchBooks(isRefreshing: Boolean = false) {
        viewModelScope.launch {
            try {
                _booksState.update {
                    it.copy(
                        isLoading = !isRefreshing,
                        isRefreshing = isRefreshing
                    )
                }

                _booksState.update {
                    it.copy(
                        isLoading = false,
                        isRefreshing = false,
                        groupedBooksList = getBooksUseCase.invoke()
                    )
                }
            } catch (e: Exception) {
                Timber.e(e, "while loading data")
            }
        }
    }

    override fun onCleared() {
        subscriptions.clear()
    }

    data class GetBooksState(
        val isLoading: Boolean = false,
        val isRefreshing: Boolean = false,
        val groupedBooksList: Map<String, List<Book>> = emptyMap()
    )
}
