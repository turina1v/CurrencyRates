package com.turina1v.currencyrates.domain.model

import com.turina1v.currencyrates.data.model.RateResponse


data class CombinedRates(
    val timestamp: Long,
    val rates: Set<CurrencyRate>
) {
    fun getRateOrNull(base: Currency, exchangeCurrency: Currency): Double? {
        val rate = rates.firstOrNull {
            it.base == base
        }
        return rate?.getRate(exchangeCurrency)
    }
}

data class CurrencyRate(
    val base: Currency,
    val exchangeRates: Map<Currency, Double>
) {
    fun getRate(currency: Currency): Double? = exchangeRates.getOrDefault(currency, null)

    companion object {
        fun fromResponse(response: RateResponse): CurrencyRate? {
            val base = Currency.getItemByNameOrNull(response.base ?: "")
            val rates = response.rates?.filter {
                Currency.getItemByNameOrNull(it.key) != null
            }?.mapKeys {
                Currency.getItemByNameOrNull(it.key)!!
            }
            return if (base != null && rates != null) {
                CurrencyRate(base, rates)
            } else {
                null
            }
        }
    }
}
