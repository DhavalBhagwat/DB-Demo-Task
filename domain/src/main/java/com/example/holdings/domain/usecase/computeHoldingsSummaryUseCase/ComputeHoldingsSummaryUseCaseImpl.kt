package com.example.holdings.domain.usecase.computeHoldingsSummaryUseCase

import com.example.holdings.domain.model.Holding
import com.example.holdings.domain.model.HoldingsSummary

/**
 * Implementation of [ComputeHoldingsSummaryUseCase] that computes a summary of financial holdings.
 */
class ComputeHoldingsSummaryUseCaseImpl : ComputeHoldingsSummaryUseCase {

    override operator fun invoke(
        holdings: List<Holding>,
        lastUpdatedMillis: Long?
    ): HoldingsSummary {
        val totalCurrent = holdings.sumOf { it.currentValue }
        val totalInvestment = holdings.sumOf { it.investmentValue }
        val totalPnl = holdings.sumOf { it.pnl }
        val totalTodaysPnl = holdings.sumOf { it.todaysPnl }
        val totalPnlPercent = if (totalInvestment == 0.0) 0.0 else (totalPnl / totalInvestment) * 100.0

        return HoldingsSummary(
            totalCurrentValue = totalCurrent,
            totalInvestment = totalInvestment,
            totalPnl = totalPnl,
            totalPnlPercent = totalPnlPercent,
            totalTodaysPnl = totalTodaysPnl,
            lastUpdatedMillis = lastUpdatedMillis
        )
    }
}
