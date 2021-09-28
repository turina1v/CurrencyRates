package com.turina1v.currencyrates.data.repository

import com.turina1v.currencyrates.data.api.NetworkClient
import com.turina1v.currencyrates.data.model.RateResponse
import com.turina1v.currencyrates.domain.repository.RatesRepository
import io.reactivex.Single

class RatesRepositoryImpl(private val client: NetworkClient) : RatesRepository {

    override fun getRate(base: String, currencies: String): Single<RateResponse> {
        return client.getRate(base, currencies)
    }
}