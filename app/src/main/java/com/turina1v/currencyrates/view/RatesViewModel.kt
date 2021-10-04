package com.turina1v.currencyrates.view

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.jakewharton.retrofit2.adapter.rxjava2.HttpException
import com.turina1v.currencyrates.domain.model.CombinedRates
import com.turina1v.currencyrates.domain.model.Currency
import com.turina1v.currencyrates.domain.model.DataError
import com.turina1v.currencyrates.domain.model.DataErrorInfo
import com.turina1v.currencyrates.domain.usecase.GetAllRatesUseCase
import com.turina1v.currencyrates.domain.usecase.GetInitialCurrenciesUseCase
import com.turina1v.currencyrates.domain.usecase.SavePreferredCurrenciesUseCase
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import timber.log.Timber
import java.net.UnknownHostException
import java.text.SimpleDateFormat
import java.util.*

class RatesViewModel(
    private val allRatesUseCase: GetAllRatesUseCase,
    initialCurrenciesUseCase: GetInitialCurrenciesUseCase,
    private val savePreferredCurrenciesUseCase: SavePreferredCurrenciesUseCase
) : ViewModel() {
    private val _preferredCurrencies: MutableLiveData<Pair<Currency, Currency>> = MutableLiveData()
    val preferredCurrencies: LiveData<Pair<Currency, Currency>>
        get() = _preferredCurrencies

    private val _latestUpdate: MutableLiveData<String> = MutableLiveData()
    val latestUpdate: LiveData<String>
        get() = _latestUpdate

    private val _exchangeResult: MutableLiveData<ExchangeResult> = MutableLiveData()
    val exchangeResult: LiveData<ExchangeResult>
        get() = _exchangeResult

    private val _error: MutableLiveData<DataErrorInfo> = MutableLiveData()
    val error: LiveData<DataErrorInfo>
        get() = _error

    private var cachedRates: CombinedRates? = null
    private var cachedCurrencyFrom: Currency? = null
    private var cachedCurrencyTo: Currency? = null
    private var currentCount: Int = 0

    private val disposables = CompositeDisposable()

    init {
        val initialCurrencies = initialCurrenciesUseCase.invoke()
        _preferredCurrencies.postValue(initialCurrencies)
        cachedCurrencyFrom = initialCurrencies.first
        cachedCurrencyTo = initialCurrencies.second
    }

    fun getRates() {
        disposables.add(
            allRatesUseCase.invoke()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    {
                        cachedRates = it
                        _latestUpdate.postValue(getDateFromTimestamp(it.timestamp))
                        countExchangeValue(currentCount)
                        if (System.currentTimeMillis() > it.timestamp + RATES_OUTDATED_INTERVAL) {
                            _error.postValue(
                                DataErrorInfo(
                                    false,
                                    DataError.DATA_OUTDATED
                                )
                            )
                        }
                    },
                    {
                        if (cachedRates == null) processError(it)
                    })
        )
    }

    private fun processError(error: Throwable) {
        when {
            error is HttpException && error.code() in 400..499 -> _error.postValue(
                DataErrorInfo(
                    true,
                    DataError.LOADING_FAILED
                )
            )
            error is HttpException && error.code() in 500..599 -> _error.postValue(
                DataErrorInfo(
                    true,
                    DataError.SERVER_UNAVAILABLE
                )
            )
            error is UnknownHostException -> _error.postValue(
                DataErrorInfo(
                    true,
                    DataError.NO_INTERNET_CONNECTION
                )
            )
        }
        Timber.tag(TAG).d(error)
    }

    fun savePreferredCurrencies(currencyPair: Pair<String, String>) {
        savePreferredCurrenciesUseCase.invoke(currencyPair)
    }

    fun setCurrencyFrom(newCurrency: Currency) {
        cachedCurrencyFrom = newCurrency
        countExchangeValue(currentCount)
    }

    fun setCurrencyTo(newCurrency: Currency) {
        cachedCurrencyTo = newCurrency
        countExchangeValue(currentCount)
    }

    fun countExchangeValue(count: Int) {
        currentCount = count
        if (cachedCurrencyFrom == null || cachedCurrencyTo == null || cachedRates == null) return
        val exchangeRate =
            if (cachedCurrencyFrom == cachedCurrencyTo) 1.0
            else cachedRates!!.getRateOrNull(cachedCurrencyFrom!!, cachedCurrencyTo!!)
        exchangeRate?.let { rate ->
            _exchangeResult.postValue(
                ExchangeResult(
                    currencyFrom = cachedCurrencyFrom!!,
                    currencyTo = cachedCurrencyTo!!,
                    rate = String.format("%.4f", rate),
                    result = String.format("%.4f", count * rate)
                )
            )
        }
    }

    private fun getDateFromTimestamp(timestamp: Long): String {
        val sdf = SimpleDateFormat("dd.MM.yyyy HH:mm", Locale.getDefault())
        val date = Date(timestamp)
        return sdf.format(date)
    }

    override fun onCleared() {
        if (!disposables.isDisposed) disposables.dispose()
        super.onCleared()
    }

    companion object {
        const val TAG = "RatesViewModel"
        const val RATES_OUTDATED_INTERVAL = 86_400_000L //24 hours in milliseconds
    }
}

data class ExchangeResult(
    val currencyFrom: Currency,
    val currencyTo: Currency,
    val rate: String,
    val result: String
)