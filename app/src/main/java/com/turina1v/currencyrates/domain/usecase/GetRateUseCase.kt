package com.turina1v.currencyrates.domain.usecase

import com.turina1v.currencyrates.data.model.RateResponse
import com.turina1v.currencyrates.domain.repository.RatesRepository
import io.reactivex.Single

class GetRateUseCase(private val repository: RatesRepository) {
    fun invoke(base: String, currencies: String): Single<RateResponse> =
        repository.getRate(base, currencies)
}