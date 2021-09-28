package com.turina1v.currencyrates.data.api

import com.turina1v.currencyrates.data.model.RateResponse
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {
    @GET("v1/latest")
    fun getRate(
        @Query("base") base: String,
        @Query("symbols") currencies: String,
        @Query("access_key") apiKey: String
    ): Single<RateResponse>
}