package com.turina1v.currencyrates.domain.repository

import com.turina1v.currencyrates.domain.model.CombinedRates
import io.reactivex.Single

interface RatesRepository {
    fun getRates(): Single<CombinedRates>
    fun saveRates(rates: CombinedRates): Single<Unit>
    fun getCurrencyPair(): Pair<String, String>
    fun saveCurrencyPair(pair: Pair<String, String>)
}