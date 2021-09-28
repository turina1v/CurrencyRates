package com.turina1v.currencyrates.domain.usecase

import com.turina1v.currencyrates.domain.model.CombinedRates
import com.turina1v.currencyrates.domain.model.Currency
import com.turina1v.currencyrates.domain.model.CurrencyRate
import com.turina1v.currencyrates.domain.repository.RatesRepository
import io.reactivex.Observable
import io.reactivex.Single

class GetAllRatesUseCase(private val repository: RatesRepository) {

    fun invoke(): Single<CombinedRates> {
        return Observable.fromIterable(Currency.values().toList()).flatMap {
            repository.getRate(it.name, it.getOtherSymbols()).toObservable()
        }.toList().map {
            CombinedRates(
                timestamp = System.currentTimeMillis(),
                rates = it.mapNotNull { response ->
                    CurrencyRate.fromResponse(response)
                }.toSet()
            )
        }
    }
}