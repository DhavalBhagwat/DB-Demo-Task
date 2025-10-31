package com.example.holdings.data.remote

import com.example.holdings.core.Result
import com.example.holdings.data.model.HoldingsResponse

/**
 * Interface defining the API for fetching holdings data.
 */
interface HoldingsApi {

    /**
     * Function to fetch the list of holdings from the remote API.
     *
     * @return [Result] of type [HoldingsResponse] list of holdings retrieved from the API.
     */
    suspend fun fetchHoldings(): Result<HoldingsResponse>
}
