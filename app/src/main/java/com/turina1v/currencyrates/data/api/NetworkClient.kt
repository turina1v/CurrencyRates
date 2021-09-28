package com.turina1v.currencyrates.data.api

import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import com.turina1v.currencyrates.data.model.RateResponse
import io.reactivex.Single
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class NetworkClient {
    private val apiService: ApiService

    init {
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build()
        apiService = retrofit.create(ApiService::class.java)
    }

    fun getRate(base: String, currencies: String): Single<RateResponse> =
        apiService.getRate(base, currencies, API_KEY)

    companion object {
        const val BASE_URL = "https://api.exchangeratesapi.io"
        const val API_KEY = "api_key"
    }
}