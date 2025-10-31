package com.example.holdings.domain.model

/**
 * Data class representing a financial holding.
 *
 * @property symbol The ticker symbol of the holding.
 * @property quantity The quantity of the holding owned.
 * @property ltp The last traded price of the holding.
 * @property avgPrice The average price at which the holding was purchased.
 * @property close The closing price of the holding.
 */
data class Holding(
    val symbol: String,
    val quantity: Int,
    val ltp: Double,
    val avgPrice: Double,
    val close: Double
) {
    /** Current value of the holding.  */
    val currentValue: Double get() = quantity.toDouble() * ltp

    /** Total investment value of the holding.  */
    val investmentValue: Double get() = quantity.toDouble() * avgPrice

    /** Profit and Loss (PnL) of the holding.  */
    val pnl: Double get() = currentValue - investmentValue

    /** Today's Profit and Loss (PnL) of the holding.  */
    val todaysPnl: Double get() = quantity.toDouble() * (ltp - close)

    /** Profit and Loss (PnL) percentage of the holding.  */
    val pnlPercent: Double
        get() = if (investmentValue == 0.0) 0.0 else (pnl / investmentValue) * 100.0
}
