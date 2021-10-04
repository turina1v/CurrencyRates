package com.turina1v.currencyrates.domain.model

enum class DataError(val isCacheExist: Boolean) {
    NO_INTERNET_CONNECTION(false),
    SERVER_UNAVAILABLE(false),
    LOADING_FAILED(false),
    DATA_OUTDATED(true)
}