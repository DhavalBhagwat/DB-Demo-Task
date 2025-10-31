package com.example.holdings.data.model

import kotlinx.serialization.Serializable

/**
 * Data class representing the response structure for holdings data.
 *
 * @property data [HoldingsData] containing the user's holdings information.
 */
@Serializable
data class HoldingsResponse(
    val data: HoldingsData
)
