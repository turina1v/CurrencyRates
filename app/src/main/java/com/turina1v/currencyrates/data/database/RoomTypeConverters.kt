package com.turina1v.currencyrates.data.database

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.turina1v.currencyrates.domain.model.CurrencyRate

object RoomTypeConverters {
    @TypeConverter
    @JvmStatic
    fun ratesToString(rates: Set<CurrencyRate>): String {
        return Gson().toJson(rates)
    }

    @TypeConverter
    @JvmStatic
    fun stringToRates(json: String): Set<CurrencyRate> {
        val setType = object : TypeToken<Set<CurrencyRate>>() {}.type
        return Gson().fromJson(json, setType)
    }
}