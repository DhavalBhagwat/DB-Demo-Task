package com.example.holdings.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.Serializable

/**
 * Data class representing a financial holding entity in the local database.
 *
 * @property symbol [String] unique symbol identifier for the holding.
 * @property quantity [Double] quantity of the holding owned.
 * @property avgPrice [Double] average price at which the holding was acquired.
 * @property ltp [Double] last traded price of the holding.
 * @property close [Double] closing price of the holding.
 */
@Serializable
@Entity(tableName = "holdings")
data class HoldingEntity(
    @PrimaryKey val symbol: String,
    val quantity: Int,
    val ltp: Double,
    val avgPrice: Double,
    val close: Double
)
