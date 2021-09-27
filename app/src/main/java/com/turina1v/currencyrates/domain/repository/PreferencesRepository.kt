package com.turina1v.currencyrates.domain.repository

interface PreferencesRepository {
    fun getCurrencies(): Pair<String, String>
    fun setCurrencies(currencyPair: Pair<String, String>)
}