package com.amamode.realestatemanager.di

import com.amamode.realestatemanager.data.repository.EstateRepository
import com.amamode.realestatemanager.domain.services.EstateService
import org.koin.dsl.module

val servicesModule = module {
    single<EstateService> {
        EstateRepository(
            get()
        )
    }
}

