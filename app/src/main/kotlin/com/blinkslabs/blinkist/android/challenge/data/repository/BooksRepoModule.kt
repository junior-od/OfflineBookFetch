package com.blinkslabs.blinkist.android.challenge.data.repository

import com.blinkslabs.blinkist.android.challenge.data.api.BooksApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object BooksRepoModule {

    @Provides
    @Singleton
    fun providesBooksRepo(booksApi: BooksApi): BooksRepo = BooksRepoImpl(booksApi)
}