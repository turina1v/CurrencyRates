package com.turina1v.currencyrates.data.repository

import android.content.Context
import com.turina1v.currencyrates.domain.model.Currency
import com.turina1v.currencyrates.domain.repository.PreferencesRepository

class PreferencesRepositoryImpl(context: Context) : PreferencesRepository {
    private val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    override fun getCurrencies(): Pair<String, String> {
        return Pair(
            prefs.getString(PREFS_KEY_FROM, Currency.RUB.name) ?: Currency.RUB.name,
            prefs.getString(PREFS_KEY_TO, Currency.USD.name) ?: Currency.USD.name
        )
    }

    override fun setCurrencies(currencyPair: Pair<String, String>) {
        with(prefs.edit()) {
            putString(PREFS_KEY_FROM, currencyPair.first)
            putString(PREFS_KEY_TO, currencyPair.second)
            apply()
        }
    }

    companion object {
        private const val PREFS_NAME = "currency_preferences"
        private const val PREFS_KEY_FROM = "from"
        private const val PREFS_KEY_TO = "to"
    }
}