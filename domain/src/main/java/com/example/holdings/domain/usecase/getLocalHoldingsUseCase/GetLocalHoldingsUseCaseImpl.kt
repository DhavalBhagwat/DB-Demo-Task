package com.example.holdings.domain.usecase.getLocalHoldingsUseCase

import com.example.holdings.domain.model.Holding
import com.example.holdings.domain.repository.HoldingsRepository
import kotlinx.coroutines.flow.Flow

/**
 * Implementation of [GetLocalHoldingsUseCase] that retrieves local financial holdings data.
 *
 * @property repository [HoldingsRepository] for accessing holdings data.
 */
class GetLocalHoldingsUseCaseImpl(
    private val repository: HoldingsRepository
) : GetLocalHoldingsUseCase {
    override operator fun invoke(): Flow<List<Holding>> = repository.getLocalHoldings()
}
