package com.amamode.realestatemanager.di

import com.amamode.realestatemanager.data.room.EstateDao
import com.amamode.realestatemanager.data.room.EstateRoomDatabase
import org.koin.dsl.module

val daoModule = module {
    single<EstateDao>{
        EstateRoomDatabase.getDatabase(get()).estateDao()
    }
}
