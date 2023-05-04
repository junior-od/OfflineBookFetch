package com.blinkslabs.blinkist.android.challenge.data.api

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@InstallIn(SingletonComponent::class)
@Module
interface BooksApiModule {

    @Binds fun bindsBooksApi(mockBooksApi: MockBooksApi): BooksApi
}
