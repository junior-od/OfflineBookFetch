package com.blinkslabs.blinkist.android.challenge.ui

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.blinkslabs.blinkist.android.challenge.databinding.ActivityBooksBinding
import com.blinkslabs.blinkist.android.challenge.ui.theme.BlinkistChallengeTheme
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class BooksActivity : AppCompatActivity() {

    // todo comment this
    @Inject lateinit var booksViewModelFactory: BooksViewModelFactory

    // todo let BooksViewModel use @Hiltviewmodel
    private val viewModel by viewModels<BooksViewModel> { booksViewModelFactory }

    private lateinit var binding: ActivityBooksBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        binding = ActivityBooksBinding.inflate(layoutInflater)
        setContentView(binding.root).apply {
            binding.composeView.apply {
                setViewCompositionStrategy(
                    ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed
                )

                setContent {
                    BlinkistChallengeTheme {
                        Surface(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(MaterialTheme.colors.background)
                        ) {
                            val bookListState by viewModel.books.collectAsStateWithLifecycle()
                            val bookFilterState by viewModel.filterBook.collectAsStateWithLifecycle()
                            BooksScreen(
                                getBooksState = bookListState,
                                filterBooksState = bookFilterState,
                                onRefresh = {
                                    viewModel.fetchBooks(
                                        isRefreshing = true
                                    )
                                },
                                onFilter = { isAscending, filterType ->
                                    viewModel.filterBooks(
                                        isAscending = isAscending,
                                        bookFilter = filterType
                                    )
                                }

                            )
                        }
                    }
                }
            }
        }
    }
}
