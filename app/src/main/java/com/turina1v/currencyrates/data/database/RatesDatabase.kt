package com.turina1v.currencyrates.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.turina1v.currencyrates.domain.model.CombinedRates

@Database(entities = [CombinedRates::class], version = 1, exportSchema = false)
@TypeConverters(RoomTypeConverters::class)
abstract class RatesDatabase : RoomDatabase() {
    abstract fun getRatesDao(): RatesDao

    companion object {
        private const val databaseName = "rates_database"

        fun create(applicationContext: Context): RatesDatabase {
            return Room
                .databaseBuilder(applicationContext, RatesDatabase::class.java, databaseName)
                .fallbackToDestructiveMigration()
                .build()
        }
    }
}