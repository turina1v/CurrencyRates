package com.turina1v.currencyrates.data.repository

import android.content.Context
import com.turina1v.currencyrates.data.api.NetworkClient
import com.turina1v.currencyrates.data.database.RatesDao
import com.turina1v.currencyrates.data.model.RateResponse
import com.turina1v.currencyrates.domain.model.CombinedRates
import com.turina1v.currencyrates.domain.model.Currency
import com.turina1v.currencyrates.domain.model.CurrencyRate
import com.turina1v.currencyrates.domain.repository.RatesRepository
import io.reactivex.Observable
import io.reactivex.Single

class RatesRepositoryImpl(
    context: Context,
    private val client: NetworkClient,
    private val dao: RatesDao
) : RatesRepository {

    private val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
    private var cachedRates: CombinedRates? = null

    override fun getRates(): Single<CombinedRates> {
        cachedRates?.let {
            return if (System.currentTimeMillis() < it.timestamp + CACHE_INTERVAL) Single.just(it)
            else getRatesFromNetwork()
        } ?: return getRatesFromDb().onErrorResumeNext { getRatesFromNetwork() }
    }

    override fun saveRates(rates: CombinedRates): Single<Unit> {
        return dao.insertRates(rates)
    }

    override fun getCurrencyPair(): Pair<String, String> {
        return Pair(
            prefs.getString(PREFS_KEY_FROM, Currency.RUB.name) ?: Currency.RUB.name,
            prefs.getString(PREFS_KEY_TO, Currency.USD.name) ?: Currency.USD.name
        )
    }

    override fun saveCurrencyPair(pair: Pair<String, String>) {
        with(prefs.edit()) {
            putString(PREFS_KEY_FROM, pair.first)
            putString(PREFS_KEY_TO, pair.second)
            apply()
        }
    }

    private fun getRate(base: String, currencies: String): Single<RateResponse> {
        return client.getRate(base, currencies)
    }

    private fun getRatesFromNetwork(): Single<CombinedRates> {
        return Observable.fromIterable(Currency.values().toList()).flatMap {
            getRate(it.name, it.getOtherSymbols()).toObservable()
        }.toList().map {
            CombinedRates(
                timestamp = System.currentTimeMillis(),
                rates = it.mapNotNull { response ->
                    CurrencyRate.fromResponse(response)
                }.toSet()
            )
        }
    }

    private fun getRatesFromDb(): Single<CombinedRates> {
        return dao.getRates()
    }

    companion object {
        private const val PREFS_NAME = "currency_preferences"
        private const val PREFS_KEY_FROM = "from"
        private const val PREFS_KEY_TO = "to"

        /** One hour in milliseconds */
        private const val CACHE_INTERVAL = 3_600_000L
    }
}