package com.example.holdings.domain.usecase.computeHoldingsSummaryUseCase

import com.example.holdings.domain.model.Holding
import com.example.holdings.domain.model.HoldingsSummary

/**
 * Use case interface for computing a summary of financial holdings.
 */
interface ComputeHoldingsSummaryUseCase {
    operator fun invoke(holdings: List<Holding>, lastUpdatedMillis: Long? = null): HoldingsSummary
}
