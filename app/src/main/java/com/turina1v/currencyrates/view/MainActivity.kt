package com.turina1v.currencyrates.view

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import com.google.android.material.button.MaterialButton
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
        observeViewModel()
        listenToTextChanges()
        setUpButtons()
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
            latestUpdateText.text = it.toString()
        }

        viewModel.preferredCurrencies.observe(this) {
            setInitialCurrencies(it)
        }

        viewModel.exchangeValue.observe(this) {
            exchangeValueText.setText(it.toString())
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
                R.id.toggleGroupFrom -> viewModel.setCurrencyFrom(it)
                R.id.toggleGroupTo -> viewModel.setCurrencyTo(it)
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
}