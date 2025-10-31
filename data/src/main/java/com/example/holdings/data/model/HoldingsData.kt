package com.example.holdings.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Data class representing the holdings data structure.
 *
 * @property userHolding List of [HoldingDto] representing the user's holdings.
 */
@Serializable
data class HoldingsData(
    @SerialName("userHolding")
    val userHolding: List<HoldingDto>
)
