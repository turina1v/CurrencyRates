package com.turina1v.currencyrates.domain.model

import com.turina1v.currencyrates.data.model.RateResponse


data class CombinedRates(
    val timestamp: Long,
    val rates: Set<CurrencyRate>
)

data class CurrencyRate(
    val base: Currency,
    val exchangeRates: Map<Currency, Double>
) {
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
