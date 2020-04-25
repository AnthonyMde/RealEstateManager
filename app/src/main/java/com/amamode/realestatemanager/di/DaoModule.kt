package com.amamode.realestatemanager.di

import com.amamode.realestatemanager.data.EstateDao
import com.amamode.realestatemanager.data.EstateRoomDatabase
import org.koin.dsl.module

val daoModule = module {
    single<EstateDao>{
        EstateRoomDatabase.getDatabase(get()).estateDao()
    }
}
