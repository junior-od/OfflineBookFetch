package com.blinkslabs.blinkist.android.challenge.ui

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.blinkslabs.blinkist.android.challenge.R
import com.blinkslabs.blinkist.android.challenge.data.model.Book
import com.blinkslabs.blinkist.android.challenge.ui.common.LoadingBooksShimmer
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun BooksScreen(
    modifier: Modifier = Modifier,
    viewModelState: BooksViewModel.GetBooksState,
    onRefresh: () -> Unit
) {
    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = { }
    ) { _ ->

        val refreshState = rememberPullRefreshState(
            refreshing = viewModelState.isRefreshing,
            onRefresh = onRefresh
        )
        Box(
            modifier = Modifier
                .fillMaxSize()
                .pullRefresh(
                    refreshState
                )
        ) {
            if (viewModelState.isLoading) {
                LoadingBooksShimmer()
            } else {
                MyBooks(
                    groupedBooksList = viewModelState.groupedBooksList
                )
            }

            PullRefreshIndicator(
                viewModelState.isRefreshing,
                refreshState,
                Modifier.align(Alignment.TopCenter)
            )
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun MyBooks(
    groupedBooksList: Map<String, List<Book>>,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier.fillMaxSize()
    ) {
        groupedBooksList.forEach { (header, booksList) ->

            stickyHeader {
                Text(header)
            }

            items(booksList) {
                SingleBook(
                    book = it
                )
            }
        }
    }
}

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun SingleBook(
    modifier: Modifier = Modifier,
    book: Book
) {
    Row(
        modifier = modifier.padding(all = 16.dp)
    ) {
        GlideImage(
            model = book.coverImageUrl,
            contentDescription = stringResource(id = R.string.cover_image),
            modifier = Modifier.size(64.dp)
        )

        Column(
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.weight(1f).padding(16.dp)
        ) {
            Text(text = book.name)
            Text(text = book.author)
        }
    }
}

@Preview
@Composable
fun BooksScreenPreview() {
    MaterialTheme {
        val bookState = BooksViewModel.GetBooksState(
            isLoading = false,
            groupedBooksList = emptyMap()
        )
        BooksScreen(
            viewModelState = bookState,
            onRefresh = {
            }
        )
    }
}
