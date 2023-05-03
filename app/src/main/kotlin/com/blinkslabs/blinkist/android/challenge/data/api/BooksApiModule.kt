package com.blinkslabs.blinkist.android.challenge.data.api

import dagger.Binds
import dagger.Module

@Module
interface BooksApiModule {

    @Binds fun bindsBooksApi(mockBooksApi: MockBooksApi): BooksApi
}
