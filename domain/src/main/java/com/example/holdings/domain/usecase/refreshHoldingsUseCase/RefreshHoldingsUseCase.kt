package com.example.holdings.domain.usecase.refreshHoldingsUseCase

import com.example.holdings.core.Result
import com.example.holdings.domain.model.Holding

/**
 * Use case interface for refreshing financial holdings data.
 */
interface RefreshHoldingsUseCase {

    /**
     * Invokes the use case to refresh holdings data.
     *
     * @return [Result] containing a [List] of [Holding] objects on success, or an error message on failure.
     */
    suspend operator fun invoke(): Result<List<Holding>>
}
