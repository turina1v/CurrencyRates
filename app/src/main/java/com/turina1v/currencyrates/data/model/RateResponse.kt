package com.turina1v.currencyrates.data.model

data class RateResponse(
	val date: String? = null,
	val success: Boolean? = null,
	val rates: Map<String, Double>? = null,
	val timestamp: Int? = null,
	val base: String? = null
)
