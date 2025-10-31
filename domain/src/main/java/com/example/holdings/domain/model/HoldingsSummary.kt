package com.example.holdings.domain.model

/**
 * Data class representing a summary of financial holdings.
 *
 * @property totalCurrentValue [Double] total current value of all holdings.
 * @property totalInvestment [Double] total investment value of all holdings.
 * @property totalPnl [Double] total profit and loss (PnL) of all holdings.
 * @property totalPnlPercent [Double] total profit and loss (PnL) percentage of all holdings.
 * @property totalTodaysPnl [Double] total today's profit and loss (PnL) of all holdings.
 * @property lastUpdatedMillis [Long?] timestamp of the last update in milliseconds.
 */
data class HoldingsSummary(
    val totalCurrentValue: Double = 0.0,
    val totalInvestment: Double = 0.0,
    val totalPnl: Double = 0.0,
    val totalPnlPercent: Double = 0.0,
    val totalTodaysPnl: Double = 0.0,
    val lastUpdatedMillis: Long? = null
)
