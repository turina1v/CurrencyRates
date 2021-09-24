package com.turina1v.currencyrates.di

import com.turina1v.currencyrates.data.api.NetworkClient
import com.turina1v.currencyrates.data.repository.RatesRepositoryImpl
import com.turina1v.currencyrates.domain.GetLatestRatesUseCase
import com.turina1v.currencyrates.domain.RatesRepository
import com.turina1v.currencyrates.view.RatesViewModel
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    single { NetworkClient() }
    single<RatesRepository> { RatesRepositoryImpl(get()) }
    single { GetLatestRatesUseCase(get()) }
    viewModel { RatesViewModel(get()) }
}