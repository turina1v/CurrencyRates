package com.turina1v.currencyrates.domain.model

enum class Currency {
    RUB,
    USD,
    EUR,
    GBP,
    CHF,
    CNY;

    fun getOtherSymbols(): String {
        return values().filter {
            it != this
        }.joinToString(separator = ",")
    }

    companion object {
        fun getItemByNameOrNull(name: String): Currency? {
            return values().firstOrNull {
                it.name == name
            }
        }
    }
}