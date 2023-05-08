package com.blinkslabs.blinkist.android.challenge.di

import android.app.Application
import android.content.Context
import com.blinkslabs.blinkist.android.challenge.data.api.BooksApiModule
import com.blinkslabs.blinkist.android.challenge.data.local.BookDatabaseModule
import com.blinkslabs.blinkist.android.challenge.data.network.ConnectionModule
import com.blinkslabs.blinkist.android.challenge.data.repository.BooksRepoModule
import com.blinkslabs.blinkist.android.challenge.ui.BooksActivity
import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(
    modules = [
        BooksApiModule::class,
        BooksRepoModule::class,
        BookDatabaseModule::class,
        ConnectionModule::class
    ]
)
interface ApplicationComponent {

    @Component.Factory
    interface Factory {
        fun create(@BindsInstance application: Application): ApplicationComponent
    }

    fun inject(activity: BooksActivity)
}
