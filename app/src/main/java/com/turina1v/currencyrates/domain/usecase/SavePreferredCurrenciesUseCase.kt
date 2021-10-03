package com.turina1v.currencyrates.domain.usecase

import com.turina1v.currencyrates.domain.repository.RatesRepository

class SavePreferredCurrenciesUseCase(private val repository: RatesRepository) {
    fun invoke(currencyPair: Pair<String, String>) {
        repository.saveCurrencyPair(currencyPair)
    }
}