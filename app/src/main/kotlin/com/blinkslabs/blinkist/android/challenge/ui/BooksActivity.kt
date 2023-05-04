package com.blinkslabs.blinkist.android.challenge.ui

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.blinkslabs.blinkist.android.challenge.data.model.Book
import com.blinkslabs.blinkist.android.challenge.databinding.ActivityBooksBinding
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber
import javax.inject.Inject

@AndroidEntryPoint
class BooksActivity : AppCompatActivity() {

    // todo comment this
    @Inject lateinit var booksViewModelFactory: BooksViewModelFactory

    // todo let BooksViewModel use @Hiltviewmodel
    private val viewModel by viewModels<BooksViewModel> { booksViewModelFactory }

    private lateinit var recyclerAdapter: BookListRecyclerAdapter

    private lateinit var recyclerView: RecyclerView
    private lateinit var swipeRefreshLayout: SwipeRefreshLayout

    private lateinit var binding: ActivityBooksBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        binding = ActivityBooksBinding.inflate(layoutInflater)
        setContentView(binding.root).apply {
            binding.composeView.apply {
                setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)

                setContent {
                    MaterialTheme {
                        Text("ddd")
                    }
                }
            }
        }
//        recyclerView = findViewById(R.id.recyclerView)
//        swipeRefreshLayout = findViewById(R.id.swipeRefreshLayout)

//        (application as BlinkistChallengeApplication).component.inject(this)

//        recyclerView.layoutManager = LinearLayoutManager(this)
//        recyclerAdapter = BookListRecyclerAdapter()
//        recyclerView.adapter = recyclerAdapter
//
//        swipeRefreshLayout.setOnRefreshListener { viewModel.fetchBooks() }
//
        viewModel.books().observe(
            this,
            Observer { books ->
                Timber.tag("yeah").d(books?.get(0)?.author.toString())

//            showBooks(books)
//            hideLoading()
            }
        )
//
//        showLoading()
        viewModel.fetchBooks()
    }

    private fun showLoading() {
        swipeRefreshLayout.isRefreshing = true
    }

    private fun hideLoading() {
        swipeRefreshLayout.isRefreshing = false
    }

    private fun showBooks(books: List<Book>) {
        recyclerAdapter.setItems(books)
        recyclerAdapter.notifyDataSetChanged()
        swipeRefreshLayout.isRefreshing = false
    }
}
