package com.amamode.realestatemanager.di

import com.amamode.realestatemanager.data.EstateRepository
import com.amamode.realestatemanager.domain.EstateService
import org.koin.dsl.module

val servicesModule = module {
    single<EstateService> { EstateRepository(get()) }
}

