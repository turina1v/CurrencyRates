package com.turina1v.currencyrates.domain.usecase

import com.turina1v.currencyrates.domain.repository.PreferencesRepository

class SavePreferredCurrenciesUseCase(private val repository: PreferencesRepository) {
    fun invoke(currencyPair: Pair<String, String>) {
        repository.setCurrencies(currencyPair)
    }
}