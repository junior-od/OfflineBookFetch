package com.blinkslabs.blinkist.android.challenge

import android.app.Application
import com.blinkslabs.blinkist.android.challenge.di.ApplicationComponent
import com.blinkslabs.blinkist.android.challenge.di.DaggerApplicationComponent
import com.jakewharton.threetenabp.AndroidThreeTen
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber

@HiltAndroidApp
class BlinkistChallengeApplication : Application() {

    val component: ApplicationComponent by lazy {
        DaggerApplicationComponent.factory().create(applicationContext)
    }

    override fun onCreate() {
        super.onCreate()
        initTimber()
        initThreeTen()
    }

    private fun initTimber() {
        if (BuildConfig.DEBUG) Timber.plant(Timber.DebugTree())
    }

    private fun initThreeTen() = AndroidThreeTen.init(this)
}
