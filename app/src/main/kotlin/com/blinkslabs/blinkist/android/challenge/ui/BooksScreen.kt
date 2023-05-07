package com.blinkslabs.blinkist.android.challenge.ui

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Tab
import androidx.compose.material.TabRow
import androidx.compose.material.Text
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import coil.compose.AsyncImage
import com.blinkslabs.blinkist.android.challenge.R
import com.blinkslabs.blinkist.android.challenge.data.model.Book
import com.blinkslabs.blinkist.android.challenge.ui.common.LoadingBooksShimmer
import com.blinkslabs.blinkist.android.challenge.util.Constants
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun BooksScreen(
    modifier: Modifier = Modifier,
    getBooksState: BooksViewModel.GetBooksState,
    filterBooksState: BooksViewModel.FilterBooksState,
    onRefresh: () -> Unit,
    onFilter: (isAscending: Boolean, filterType: Constants.BookFilters) -> Unit
) {
    var showFilter by rememberSaveable {
        mutableStateOf(false)
    }

    val booksLazyListState = rememberLazyListState()
    val scope = rememberCoroutineScope()

    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            TopBar(
                filterBooksState = filterBooksState,
                onOrderClicked = { isAscending, bookFilter ->
                    onFilter(isAscending, bookFilter)
                    scope.launch {
                        booksLazyListState.scrollToItem(index = 0)
                    }
                },
                onFilterIconClicked = {
                    showFilter = true
                }
            )
        }
    ) { _ ->

        val refreshState = rememberPullRefreshState(
            refreshing = getBooksState.isRefreshing,
            onRefresh = onRefresh
        )
        Box(
            modifier = Modifier
                .fillMaxSize()
                .pullRefresh(
                    refreshState
                )
        ) {
            if (getBooksState.isLoading) {
                LoadingBooksShimmer()
            } else {
                MyBooks(
                    lazyListState = booksLazyListState,
                    groupedBooksList = getBooksState.groupedBooksList
                )
            }

            PullRefreshIndicator(
                getBooksState.isRefreshing,
                refreshState,
                Modifier.align(Alignment.TopCenter)
            )

            if (showFilter) {
                FilterDialog(
                    filterBookState = filterBooksState,
                    onFilterSelected = { isAscending, filterType ->
                        onFilter(isAscending, filterType)
                        showFilter = false
                        scope.launch {
                            booksLazyListState.scrollToItem(index = 0)
                        }
                    },
                    onDismissDialog = {
                        showFilter = false
                    }

                )
            }
        }
    }
}

@Composable
fun FilterDialog(
    modifier: Modifier = Modifier,
    filterBookState: BooksViewModel.FilterBooksState,
    onDismissDialog: () -> Unit,
    onFilterSelected: (
        isAscending: Boolean,
        filterType: Constants.BookFilters
    ) -> Unit
) {
    Dialog(
        onDismissRequest = onDismissDialog
    ) {
        Column(
            modifier = modifier
                .background(MaterialTheme.colors.background)
                .padding(16.dp)
        ) {
            Text(
                text = stringResource(id = R.string.filter_by),
                color = MaterialTheme.colors.onSurface,
                style = MaterialTheme.typography.body1
            )

            Spacer(modifier = Modifier.padding(vertical = 10.dp))

            var selectedTab by remember {
                when (filterBookState.bookFilter) {
                    Constants.BookFilters.WEEK_OF_YEAR -> mutableStateOf(0)
                    Constants.BookFilters.ALPHABET -> mutableStateOf(1)
                }
            }

            TabRow(
                selectedTabIndex = selectedTab,
                backgroundColor = MaterialTheme.colors.background,
                contentColor = MaterialTheme.colors.onSurface
            ) {
                Tab(
                    selected = selectedTab == 0,
                    onClick = {
                        if (selectedTab != 0) {
                            onFilterSelected(
                                filterBookState.isAscending,
                                Constants.BookFilters.WEEK_OF_YEAR
                            )
                        } else {
                            onDismissDialog()
                        }
                    }
                ) {
                    Text(text = stringResource(id = R.string.week))
                }
                Tab(
                    selected = selectedTab == 1,
                    onClick = {
                        if (selectedTab != 1) {
                            onFilterSelected(
                                filterBookState.isAscending,
                                Constants.BookFilters.ALPHABET
                            )
                        } else {
                            onDismissDialog()
                        }
                    }
                ) {
                    Text(text = stringResource(id = R.string.alphabet))
                }
            }
        }
    }
}

@Composable
fun TopBar(
    modifier: Modifier = Modifier,
    filterBooksState: BooksViewModel.FilterBooksState,
    onOrderClicked: (
        isAscending: Boolean,
        filterType: Constants.BookFilters
    ) -> Unit,
    onFilterIconClicked: () -> Unit
) {
    Card(
        elevation = 3.dp
    ) {
        Row(
            horizontalArrangement = Arrangement.End,
            modifier = modifier
                .fillMaxWidth()
                .height(50.dp)
                .background(MaterialTheme.colors.background)
                .padding(end = 10.dp)
        ) {
            IconButton(
                onClick = {
                    onOrderClicked(
                        !filterBooksState.isAscending,
                        filterBooksState.bookFilter
                    )
                }
            ) {
                Icon(
                    painter = if (filterBooksState.isAscending) painterResource(id = R.drawable.ic_arrow_up) else painterResource(id = R.drawable.ic_arrow_down),
                    contentDescription = stringResource(id = R.string.order_type),
                    tint = MaterialTheme.colors.onSurface
                )
            }

            Spacer(modifier = Modifier.padding(horizontal = 5.dp))

            IconButton(
                onClick = onFilterIconClicked
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_filter),
                    contentDescription = stringResource(id = R.string.filter),
                    tint = MaterialTheme.colors.onSurface
                )
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun MyBooks(
    groupedBooksList: Map<String, List<Book>>,
    lazyListState: LazyListState,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier.fillMaxSize(),
        state = lazyListState
    ) {
        groupedBooksList.forEach { (header, booksList) ->

            stickyHeader {
                HeaderItem(header)
            }

            items(booksList) {
                SingleBook(
                    book = it
                )
            }
        }
    }
}

@Composable
fun HeaderItem(
    text: String,
    modifier: Modifier = Modifier
) {
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .clip(
                RoundedCornerShape(
                    topEnd = 20.dp,
                    bottomEnd = 20.dp
                )
            )
            .background(MaterialTheme.colors.onBackground)
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.body1,
            color = Color.White,
            modifier = Modifier.padding(all = 10.dp)
        )
    }
}

@Composable
fun SingleBook(
    modifier: Modifier = Modifier,
    book: Book
) {
    Column(
        modifier = modifier.padding(all = 16.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            AsyncImage(
                model = book.coverImageUrl,
                contentDescription = stringResource(id = R.string.cover_image),
                modifier = Modifier.size(64.dp)
            )

            Column(
                verticalArrangement = Arrangement.Center,
                modifier = Modifier
                    .weight(1f)
                    .padding(16.dp)
            ) {
                Text(
                    text = book.name,
                    style = MaterialTheme.typography.body2,
                    color = MaterialTheme.colors.onSurface
                )

                Spacer(modifier = Modifier.height(5.dp))

                Text(
                    text = book.author,
                    style = MaterialTheme.typography.subtitle1,
                    color = MaterialTheme.colors.onSurface
                )
            }
        }

        Spacer(modifier = Modifier.height(10.dp))
        Spacer(
            modifier = Modifier
                .background(Color.DarkGray)
                .height(1.dp)
                .fillMaxWidth()

        )
    }
}

@Preview
@Composable
fun BooksScreenPreview() {
    MaterialTheme {
        val bookState = BooksViewModel.GetBooksState()
        val filterBooksState = BooksViewModel.FilterBooksState()
        BooksScreen(
            getBooksState = bookState,
            filterBooksState = filterBooksState,
            onRefresh = {
            },
            onFilter = { _, _ ->
            }
        )
    }
}
