package com.example.holdings.domain.repository

import com.example.holdings.core.Result
import com.example.holdings.domain.model.Holding
import kotlinx.coroutines.flow.Flow

/**
 * Repository interface for managing financial holdings data.
 */
interface HoldingsRepository {

    /**
     * Function to retrieve the list of holdings from the local database.
     *
     * @return [Flow] emitting a [List] of [Holding] objects.
     */
    fun getLocalHoldings(): Flow<List<Holding>>

    /**
     * Function to fetch the list of holdings from the remote API.
     *
     * @return [Result] containing a [List] of [Holding] objects on success, or an error message on failure.
     */
    suspend fun fetchRemoteHoldings(): Result<List<Holding>>

    /**
     * Function to save a list of holdings to the local database.
     *
     * @param holdings [List] of [Holding] objects to be saved.
     */
    suspend fun saveHoldings(holdings: List<Holding>)
}
