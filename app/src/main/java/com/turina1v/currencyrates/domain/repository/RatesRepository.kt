package com.turina1v.currencyrates.domain.repository

import com.turina1v.currencyrates.data.model.RateResponse
import io.reactivex.Single

interface RatesRepository {
    fun getRate(base: String, currencies: String): Single<RateResponse>
}