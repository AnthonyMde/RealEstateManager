package com.amamode.realestatemanager

import android.app.Application
import timber.log.Timber

class AndroidApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
    }
}
