package com.turina1v.currencyrates.domain.repository

import com.turina1v.currencyrates.domain.model.CombinedRates
import io.reactivex.Single

interface DatabaseRatesRepository {
    fun getRates(): Single<CombinedRates>
    fun saveRates(rates: CombinedRates): Single<Unit>
}