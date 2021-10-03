package com.turina1v.currencyrates.domain.usecase

import com.turina1v.currencyrates.domain.model.Currency
import com.turina1v.currencyrates.domain.repository.RatesRepository

class GetInitialCurrenciesUseCase(private val repository: RatesRepository) {
    fun invoke(): Pair<Currency, Currency> {
        val stringPair = repository.getCurrencyPair()
        return Pair(
            Currency.getItemByNameOrNull(stringPair.first) ?: Currency.RUB,
            Currency.getItemByNameOrNull(stringPair.second) ?: Currency.USD
        )
    }
}