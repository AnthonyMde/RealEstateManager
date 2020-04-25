package com.amamode.realestatemanager

import android.app.Application
import com.amamode.realestatemanager.di.servicesModule
import com.amamode.realestatemanager.di.viewModelsModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import timber.log.Timber

class AndroidApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        setUpKoin()

        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
    }

    private fun setUpKoin() {
        startKoin {
            androidLogger()
            androidContext(this@AndroidApplication)
            modules(
                servicesModule,
                viewModelsModule
            )
        }
    }
}
