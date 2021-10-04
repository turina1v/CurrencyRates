package com.turina1v.currencyrates.domain.model

enum class DataError {
    NO_INTERNET_CONNECTION,
    SERVER_UNAVAILABLE,
    LOADING_FAILED,
    DATA_OUTDATED
}

data class DataErrorInfo(
    val isInitial: Boolean,
    val error: DataError
)