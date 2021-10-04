package com.turina1v.currencyrates.view

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import com.google.android.material.button.MaterialButton
import com.turina1v.currencyrates.R
import com.turina1v.currencyrates.domain.model.Currency
import com.turina1v.currencyrates.domain.model.DataError
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.layout_error.*
import kotlinx.android.synthetic.main.layout_exchange.*
import org.koin.android.viewmodel.ext.android.viewModel

class MainActivity : AppCompatActivity() {

    private val viewModel: RatesViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        observeViewModel()
        listenToTextChanges()
        setUpButtons()
    }

    override fun onResume() {
        super.onResume()
        viewModel.getRates()
    }

    override fun onStop() {
        super.onStop()
        setPreferredCurrencies()
    }

    private fun observeViewModel() {

        viewModel.latestUpdate.observe(this) {
            if (layoutLoader.isVisible) {
                layoutLoader.isVisible = false
                layoutExchange.isVisible = true
            }
            latestUpdateText.text = it
            latestUpdateText.text = getString(R.string.latest_update_message, it)
        }

        viewModel.preferredCurrencies.observe(this) {
            setInitialCurrencies(it)
        }

        viewModel.exchangeResult.observe(this) {
            exchangeValueText.setText(it.result)
            currentRateText.text =
                getString(R.string.current_rate_message, it.currencyFrom.name, it.rate, it.currencyTo.name)
        }

        viewModel.error.observe(this) {
            if (it.isInitial) {
                layoutLoader.isVisible = false
                layoutError.isVisible = true
                errorMessage.text = getErrorMessage(it.error)
            } else {
                ratesOutdatedText.text = getErrorMessage(it.error)
            }
        }
    }

    private fun setInitialCurrencies(currencyPair: Pair<Currency, Currency>) {
        toggleGroupFrom.checkButtonByText(currencyPair.first.name) {
            buttonFromRub.isChecked = true
        }
        toggleGroupTo.checkButtonByText(currencyPair.second.name) {
            buttonToUsd.isChecked = true
        }
    }

    private fun listenToTextChanges() {
        numberInputEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable?) {
                val count =
                    if (s.toString().isEmpty()) 0
                    else s.toString().toIntOrNull()
                count?.let {
                    viewModel.countExchangeValue(it)
                }
            }
        })
    }

    private fun setUpButtons() {
        toggleGroupFrom.addOnButtonCheckedListener { group, checkedId, isChecked ->
            if (isChecked) listenToButtonCheckedChanges(group.id, checkedId)
        }
        toggleGroupTo.addOnButtonCheckedListener { group, checkedId, isChecked ->
            if (isChecked) listenToButtonCheckedChanges(group.id, checkedId)
        }
    }

    private fun listenToButtonCheckedChanges(groupId: Int, checkedId: Int) {
        val button = findViewById<MaterialButton>(checkedId)
        val currency = Currency.getItemByNameOrNull(button.text.toString())
        currency?.let {
            when (groupId) {
                R.id.toggleGroupFrom -> {
                    fullTextFrom.text = getCurrencyFullName(it)
                    viewModel.setCurrencyFrom(it)
                }
                R.id.toggleGroupTo -> {
                    fullTextTo.text = getCurrencyFullName(it)
                    viewModel.setCurrencyTo(it)
                }
            }
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

    private fun getCurrencyFullName(currency: Currency): String {
        return when (currency) {
            Currency.RUB -> getString(R.string.currency_name_rub)
            Currency.USD -> getString(R.string.currency_name_usd)
            Currency.EUR -> getString(R.string.currency_name_eur)
            Currency.GBP -> getString(R.string.currency_name_gbp)
            Currency.CHF -> getString(R.string.currency_name_chf)
            Currency.CNY -> getString(R.string.currency_name_cny)
        }
    }

    private fun getErrorMessage(error: DataError): String {
        return when (error) {
            DataError.NO_INTERNET_CONNECTION -> getString(R.string.error_message_no_internet)
            DataError.SERVER_UNAVAILABLE -> getString(R.string.error_message_server_unavailable)
            DataError.LOADING_FAILED -> getString(R.string.error_message_loading_failed)
            DataError.DATA_OUTDATED -> getString(R.string.error_message_data_outdated)
        }
    }

    companion object {
        private const val ARG_CURRENT_COUNT = "current_count"
    }
}