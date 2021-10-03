package com.turina1v.currencyrates.view

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.turina1v.currencyrates.domain.model.CombinedRates
import com.turina1v.currencyrates.domain.model.Currency
import com.turina1v.currencyrates.domain.usecase.GetAllRatesUseCase
import com.turina1v.currencyrates.domain.usecase.GetInitialCurrenciesUseCase
import com.turina1v.currencyrates.domain.usecase.SavePreferredCurrenciesUseCase
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import timber.log.Timber
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

    private val _exchangeValue: MutableLiveData<Double> = MutableLiveData()
    val exchangeValue: LiveData<Double>
        get() = _exchangeValue

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

        disposables.add(
            allRatesUseCase.invoke()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    {
                        cachedRates = it
                        _latestUpdate.postValue(getDateFromTimestamp(it.timestamp))
                    },
                    {
                        Timber.tag(TAG).d(it)
                    })
        )
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
        exchangeRate?.let {
            _exchangeValue.postValue(count * it)
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
    }
}