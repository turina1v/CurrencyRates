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

        viewModel.latestRates.observe(this) {
            mainText.text = it
        }
    }
}