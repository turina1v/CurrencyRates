package com.turina1v.currencyrates.view

import androidx.lifecycle.ViewModel
import com.turina1v.currencyrates.domain.GetLatestRatesUseCase

class RatesViewModel(private val useCase: GetLatestRatesUseCase) : ViewModel() {

}