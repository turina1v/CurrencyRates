package com.turina1v.currencyrates.domain.usecase

import com.turina1v.currencyrates.data.model.LatestRatesResponse
import com.turina1v.currencyrates.domain.repository.RatesRepository
import io.reactivex.Single

class GetLatestRatesUseCase(private val repository: RatesRepository) {
    fun invoke(base: String, currencies: String): Single<LatestRatesResponse> =
        repository.getLatestRates(base, currencies)
}