package com.blinkslabs.blinkist.android.challenge.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.blinkslabs.blinkist.android.challenge.data.model.Book
import com.blinkslabs.blinkist.android.challenge.data.network.ConnectivityObserver
import com.blinkslabs.blinkist.android.challenge.data.repository.BooksRepo
import com.blinkslabs.blinkist.android.challenge.domain.usecase.GetBooksUseCase
import com.blinkslabs.blinkist.android.challenge.util.Constants
import io.reactivex.disposables.CompositeDisposable
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import timber.log.Timber
import javax.inject.Inject

class BooksViewModel @Inject constructor(
    private val getBooksUseCase: GetBooksUseCase,
    private val booksRepo: BooksRepo,
    private val connectivityObserver: ConnectivityObserver
) : ViewModel() {

    private val subscriptions = CompositeDisposable()

    private val _booksState = MutableStateFlow(GetBooksState())
    val books get() = _booksState.asStateFlow()

    private val _filterBooksState = MutableStateFlow(FilterBooksState())

    val filterBook get() = _filterBooksState.asStateFlow()

    private var emitBooksJob: Job? = null

    private var fetchBooksJob: Job? = null

    init {
        fetchBooks(false)

        emitBooks()
    }

    private fun emitBooks(
        isAscending: Boolean = false,
        bookFilter: Constants.BookFilters = Constants.BookFilters.WEEK_OF_YEAR
    ) {
        emitBooksJob?.cancel()

        emitBooksJob = getBooksUseCase.invoke(
            isAscending = isAscending,
            bookFilter = bookFilter
        ).onEach {
            val mappedBooks = it
            if (mappedBooks.isNotEmpty()) {
                _booksState.update { getBookState ->
                    getBookState.copy(
                        isLoading = false,
                        isRefreshing = false,
                        groupedBooksList = mappedBooks
                    )
                }
            }
        }.launchIn(viewModelScope)
    }

    fun fetchBooks(isRefreshing: Boolean = false) {
        fetchBooksJob?.cancel()

        fetchBooksJob = connectivityObserver.observe().onEach {
                status ->
            if (status == ConnectivityObserver.Status.Available) {
                try {
                    if (_booksState.value.groupedBooksList.isNotEmpty() && !isRefreshing) {
                    } else {
                        _booksState.update {
                            it.copy(
                                isLoading = !isRefreshing,
                                isRefreshing = isRefreshing
                            )
                        }
                    }

                    booksRepo.fetchBooks(
                        checkUpdates = isRefreshing
                    )

                    fetchBooksJob?.cancel()
                } catch (e: Exception) {
                    Timber.e(e, "while loading data")
                }
            } else {
                _booksState.update {
                    it.copy(
                        isLoading = false,
                        isRefreshing = false
                    )
                }
            }
        }.launchIn(viewModelScope)
    }

    fun filterBooks(
        isAscending: Boolean,
        bookFilter: Constants.BookFilters
    ) {
        emitBooks(
            isAscending = isAscending,
            bookFilter = bookFilter
        )

        _filterBooksState.update {
            it.copy(
                isAscending = isAscending,
                bookFilter = bookFilter
            )
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

    data class FilterBooksState(
        val isAscending: Boolean = false,
        val bookFilter: Constants.BookFilters = Constants.BookFilters.WEEK_OF_YEAR
    )
}
