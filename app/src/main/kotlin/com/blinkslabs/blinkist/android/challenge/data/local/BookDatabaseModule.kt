package com.blinkslabs.blinkist.android.challenge.data.local

import android.app.Application
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object BookDatabaseModule {

    @Provides
    @Singleton
    fun providesBookDatabase(application: Application): BookDatabase {
        return Room.databaseBuilder(
            application,
            BookDatabase::class.java,
            BookDatabase.DATABASE_NAME
        ).build()
    }
}
