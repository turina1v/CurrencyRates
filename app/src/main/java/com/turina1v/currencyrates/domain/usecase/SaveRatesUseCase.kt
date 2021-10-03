package com.turina1v.currencyrates.domain.usecase

import com.turina1v.currencyrates.domain.model.CombinedRates
import com.turina1v.currencyrates.domain.repository.RatesRepository
import io.reactivex.Single

class SaveRatesUseCase(private val repository: RatesRepository) {
    fun invoke(rates: CombinedRates): Single<Unit> = repository.saveRates(rates)
}