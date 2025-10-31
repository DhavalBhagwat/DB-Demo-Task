package com.example.holdings.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.holdings.data.model.HoldingEntity
import kotlinx.coroutines.flow.Flow

/**
 * Data Access Object (DAO) for managing holdings in the local database.
 */
@Dao
interface HoldingsDao {

    /**
     * Function to return all holdings as a reactive Flow.
     *
     * @return [Flow] emitting list of [HoldingEntity] objects.
     */
    @Query("SELECT * FROM holdings")
    fun getHoldings(): Flow<List<HoldingEntity>>

    /**
     * Function to insert or update a list of holdings in the database.
     *
     * @param holdings List of [HoldingEntity] objects to be inserted or updated.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertHoldings(holdings: List<HoldingEntity>)

    /**
     * Function to clear all holdings from the database.
     */
    @Query("DELETE FROM holdings")
    suspend fun clearAll()
}
