package com.turina1v.currencyrates.view

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.turina1v.currencyrates.domain.GetLatestRatesUseCase
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import timber.log.Timber

class RatesViewModel(private val useCase: GetLatestRatesUseCase) : ViewModel() {
    private val _latestRates: MutableLiveData<String> = MutableLiveData()
    val latestRates: LiveData<String>
        get() = _latestRates

    private val disposables = CompositeDisposable()

    init {
        disposables.add(useCase.invoke(base = "USD", currencies = "GBP,JPY,EUR,CAD")
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { response ->
                    _latestRates.postValue(response.toString())
                },
                {
                    Timber.d(it)
                }
            ))
    }

    override fun onCleared() {
        if (!disposables.isDisposed) disposables.dispose()
        super.onCleared()
    }

}