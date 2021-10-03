package com.turina1v.currencyrates.di

import com.turina1v.currencyrates.data.api.NetworkClient
import com.turina1v.currencyrates.data.database.RatesDatabase
import com.turina1v.currencyrates.data.repository.DatabaseRatesRepositoryImpl
import com.turina1v.currencyrates.data.repository.PreferencesRepositoryImpl
import com.turina1v.currencyrates.data.repository.RatesRepositoryImpl
import com.turina1v.currencyrates.domain.repository.DatabaseRatesRepository
import com.turina1v.currencyrates.domain.repository.PreferencesRepository
import com.turina1v.currencyrates.domain.repository.RatesRepository
import com.turina1v.currencyrates.domain.usecase.GetAllRatesUseCase
import com.turina1v.currencyrates.domain.usecase.GetInitialCurrenciesUseCase
import com.turina1v.currencyrates.domain.usecase.SavePreferredCurrenciesUseCase
import com.turina1v.currencyrates.view.RatesViewModel
import org.koin.android.ext.koin.androidApplication
import org.koin.android.ext.koin.androidContext
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    single { NetworkClient() }

    single<RatesRepository> { RatesRepositoryImpl(get()) }
    single<DatabaseRatesRepository> { DatabaseRatesRepositoryImpl(get()) }
    single<PreferencesRepository> { PreferencesRepositoryImpl(androidContext()) }

    single { GetInitialCurrenciesUseCase(get()) }
    single { GetAllRatesUseCase(get()) }
    single { SavePreferredCurrenciesUseCase(get()) }

    viewModel { RatesViewModel(get(), get(), get()) }

    single { RatesDatabase.create(androidApplication()) }
    single { get<RatesDatabase>().getRatesDao() }
}