package com.example.holdings.data.model

import kotlinx.serialization.Serializable

/**
 * Data Transfer Object (DTO) representing a financial holding.
 *
 * @property symbol [String] unique symbol identifier for the holding.
 * @property quantity [Int] quantity of the holding owned.
 * @property avgPrice [Double] average price at which the holding was acquired.
 * @property ltp [Double] last traded price of the holding.
 * @property close [Double] closing price of the holding.
 */
@Serializable
data class HoldingDto(
    val symbol: String,
    val quantity: Int,
    val ltp: Double,
    val avgPrice: Double,
    val close: Double
)
