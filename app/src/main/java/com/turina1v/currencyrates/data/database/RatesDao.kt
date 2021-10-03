package com.turina1v.currencyrates.data.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.turina1v.currencyrates.domain.model.CombinedRates
import io.reactivex.Single

@Dao
interface RatesDao {
    @Query("SELECT * FROM table_rates")
    fun getRates(): Single<CombinedRates>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertRates(rates: CombinedRates): Single<Unit>
}