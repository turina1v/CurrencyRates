package com.turina1v.currencyrates.view

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import com.turina1v.currencyrates.R
import com.turina1v.currencyrates.domain.model.Currency
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.layout_exchange.*
import org.koin.android.viewmodel.ext.android.viewModel

class MainActivity : AppCompatActivity() {

    private val viewModel: RatesViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        viewModel.latestUpdate.observe(this) {
            if (layoutLoader.isVisible) {
                layoutLoader.isVisible = false
                layoutExchange.isVisible = true
            }
            latestUpdateText.text = it.toString()
        }

        viewModel.preferredCurrencies.observe(this) {
            setInitialCurrencies(it)
        }
    }

    override fun onStop() {
        super.onStop()
        setPreferredCurrencies()
    }

    private fun setInitialCurrencies(currencyPair: Pair<Currency, Currency>) {
        toggleGroupFrom.checkButtonByText(currencyPair.first.name) {
            buttonFromRub.isChecked = true
        }
        toggleGroupTo.checkButtonByText(currencyPair.second.name) {
            buttonToUsd.isChecked = true
        }
    }

    private fun setPreferredCurrencies() {
        viewModel.savePreferredCurrencies(
            Pair(
                toggleGroupFrom.getFocusedButtonTextOrEmpty(),
                toggleGroupTo.getFocusedButtonTextOrEmpty()
            )
        )
    }
}