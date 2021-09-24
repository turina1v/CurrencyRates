package com.turina1v.currencyrates.data.repository

import com.turina1v.currencyrates.data.api.NetworkClient
import com.turina1v.currencyrates.data.model.LatestRatesResponse
import io.reactivex.Single

class RatesRepositoryImpl(private val client: NetworkClient) {
    fun getLatestRates(base: String, currencies: String): Single<LatestRatesResponse> {
        return client.getLatestRates(base, currencies)
    }
}