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

class RatesViewModel(
    private val allRatesUseCase: GetAllRatesUseCase,
    initialCurrenciesUseCase: GetInitialCurrenciesUseCase,
    private val savePreferredCurrenciesUseCase: SavePreferredCurrenciesUseCase
) : ViewModel() {
    private val _preferredCurrencies: MutableLiveData<Pair<Currency, Currency>> = MutableLiveData()
    val preferredCurrencies: LiveData<Pair<Currency, Currency>>
        get() = _preferredCurrencies

    private val _latestUpdate: MutableLiveData<Long> = MutableLiveData()
    val latestUpdate: LiveData<Long>
        get() = _latestUpdate

    private val _exchangeValue: MutableLiveData<Double> = MutableLiveData()
    val exchangeValue: LiveData<Double>
        get() = _exchangeValue

    private var cachedRates: CombinedRates? = null
    private var currentPair: Pair<Currency, Currency>? = null

    private val disposables = CompositeDisposable()

    init {
        val initialCurrencies = initialCurrenciesUseCase.invoke()
        _preferredCurrencies.postValue(initialCurrencies)
        currentPair = initialCurrencies

        disposables.add(
            allRatesUseCase.invoke()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    {
                        cachedRates = it
                        _latestUpdate.postValue(it.timestamp)
                    },
                    {
                        Timber.tag(TAG).d(it)
                    })
        )
    }

    fun savePreferredCurrencies(currencyPair: Pair<String, String>) {
        savePreferredCurrenciesUseCase.invoke(currencyPair)
    }

    fun countExchangeValue(count: Int) {
        if (currentPair == null || cachedRates == null) return
        val exchangeRate = cachedRates!!.getRateOrNull(currentPair!!.first, currentPair!!.second)
        exchangeRate?.let {
            _exchangeValue.postValue(count * it)
        }
    }

    override fun onCleared() {
        if (!disposables.isDisposed) disposables.dispose()
        super.onCleared()
    }

    companion object {
        const val TAG = "RatesViewModel"
    }
}