package com.turina1v.currencyrates.domain.usecase

import com.turina1v.currencyrates.domain.repository.PreferencesRepository

class GetInitialCurrenciesUseCase(private val repository: PreferencesRepository) {
    fun invoke(): Pair<String, String> {
        return repository.getCurrencies()
    }
}