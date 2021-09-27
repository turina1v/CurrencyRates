package com.turina1v.currencyrates.domain

import com.turina1v.currencyrates.data.model.LatestRatesResponse
import io.reactivex.Single

interface RatesRepository {
    fun getLatestRates(base: String, currencies: String): Single<LatestRatesResponse>
}