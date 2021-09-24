package com.turina1v.currencyrates.di

import com.turina1v.currencyrates.data.api.NetworkClient
import org.koin.dsl.module

val appModule = module {
    single { NetworkClient() }
}