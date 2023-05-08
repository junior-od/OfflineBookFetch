package com.blinkslabs.blinkist.android.challenge.data.network

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object ConnectionModule {
    @Provides
    @Singleton
    fun providesConnectivity(application: Application): ConnectionManagerWrapper = ConnectionManagerWrapper(
        application.applicationContext.getSystemService(
            Context.CONNECTIVITY_SERVICE
        ) as ConnectivityManager
    )

    @Provides
    @Singleton
    fun provideConnectionObserver(connectivityManager: ConnectionManagerWrapper): ConnectivityObserver = ConnectivityObserverImpl(
        connectionManager = connectivityManager
    )
}
