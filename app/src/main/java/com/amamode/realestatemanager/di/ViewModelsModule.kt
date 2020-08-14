package com.amamode.realestatemanager.di

import com.amamode.realestatemanager.ui.CurrencyViewModel
import com.amamode.realestatemanager.ui.EstateViewModel
import com.amamode.realestatemanager.ui.loan.LoanViewModel
import com.amamode.realestatemanager.ui.maps.MapsViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelsModule = module {
    viewModel { EstateViewModel(get(), get()) }
    viewModel { LoanViewModel() }
    viewModel { CurrencyViewModel(get()) }
    viewModel { MapsViewModel(get()) }
}
