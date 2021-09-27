package com.turina1v.currencyrates.view

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.turina1v.currencyrates.R
import kotlinx.android.synthetic.main.activity_main.*
import org.koin.android.viewmodel.ext.android.viewModel

class MainActivity : AppCompatActivity() {

    private val viewModel: RatesViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        viewModel.preferredCurrencies.observe(this) {
            setInitialCurrencies(it)
        }

        viewModel.latestRates.observe(this) {
            mainText.text = it
        }
    }

    override fun onStop() {
        super.onStop()
        setPreferredCurrencies()
    }

    private fun setInitialCurrencies(currencyPair: Pair<String, String>) {
        toggleGroupFrom.checkButtonByText(currencyPair.first) {
            buttonFromRub.isChecked = true
        }
        toggleGroupTo.checkButtonByText(currencyPair.second) {
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