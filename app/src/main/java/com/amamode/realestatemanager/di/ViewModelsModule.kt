package com.amamode.realestatemanager.di

import com.amamode.realestatemanager.ui.EstateViewModel
import com.amamode.realestatemanager.ui.loan.LoanViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelsModule = module {
    viewModel { EstateViewModel(get()) }
    viewModel { LoanViewModel() }
}
