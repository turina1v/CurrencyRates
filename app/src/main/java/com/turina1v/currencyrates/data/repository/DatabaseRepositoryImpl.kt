package com.turina1v.currencyrates.data.repository

import com.turina1v.currencyrates.data.database.RatesDao
import com.turina1v.currencyrates.domain.model.CombinedRates
import com.turina1v.currencyrates.domain.repository.DatabaseRatesRepository
import io.reactivex.Single

class DatabaseRatesRepositoryImpl(private val dao: RatesDao) : DatabaseRatesRepository {
    override fun getRates(): Single<CombinedRates> {
        return dao.getRates()
    }

    override fun saveRates(rates: CombinedRates): Single<Unit> {
        return dao.insertRates(rates)
    }
}