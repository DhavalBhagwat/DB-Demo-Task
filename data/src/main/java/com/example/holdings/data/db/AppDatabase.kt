package com.example.holdings.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.holdings.data.dao.HoldingsDao
import com.example.holdings.data.model.HoldingEntity

/**
 * Room database class for the holdings application.
 */
@Database(entities = [HoldingEntity::class], version = 1)
abstract class AppDatabase : RoomDatabase() {

    /**
     * Abstract function to get the [HoldingsDao].
     *
     * @return [HoldingsDao] instance for accessing holding data.
     */
    abstract fun holdingsDao(): HoldingsDao
}
