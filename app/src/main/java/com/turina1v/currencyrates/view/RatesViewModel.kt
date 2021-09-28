package com.turina1v.currencyrates.view

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.turina1v.currencyrates.domain.usecase.GetAllRatesUseCase
import com.turina1v.currencyrates.domain.usecase.GetInitialCurrenciesUseCase
import com.turina1v.currencyrates.domain.usecase.GetRateUseCase
import com.turina1v.currencyrates.domain.usecase.SavePreferredCurrenciesUseCase
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import timber.log.Timber

class RatesViewModel(
    private val rateUseCase: GetRateUseCase,
    private val allRatesUseCase: GetAllRatesUseCase,
    private val initialCurrenciesUseCase: GetInitialCurrenciesUseCase,
    private val savePreferredCurrenciesUseCase: SavePreferredCurrenciesUseCase
) : ViewModel() {
    private val _preferredCurrencies: MutableLiveData<Pair<String, String>> = MutableLiveData()
    val preferredCurrencies: LiveData<Pair<String, String>>
        get() = _preferredCurrencies
    private val _latestRates: MutableLiveData<String> = MutableLiveData()
    val latestRates: LiveData<String>
        get() = _latestRates

    private val disposables = CompositeDisposable()

    init {
        val initialCurrencies = initialCurrenciesUseCase.invoke()
        _preferredCurrencies.postValue(initialCurrencies)
        disposables.add(rateUseCase.invoke(
            base = initialCurrencies.first,
            currencies = "GBP,JPY,EUR,CAD"
        )
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { response ->
                    _latestRates.postValue(response.toString())
                },
                {
                    Timber.tag(TAG).d(it)
                }
            ))

        disposables.add(
            allRatesUseCase.invoke()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    {
                        Timber.tag(TAG).d(it.toString())
                    },
                    {
                        Timber.tag(TAG).d(it)
                    })
        )
    }

    fun savePreferredCurrencies(currencyPair: Pair<String, String>) {
        savePreferredCurrenciesUseCase.invoke(currencyPair)
    }

    override fun onCleared() {
        if (!disposables.isDisposed) disposables.dispose()
        super.onCleared()
    }

    companion object {
        const val TAG = "RatesViewModel"
    }
}