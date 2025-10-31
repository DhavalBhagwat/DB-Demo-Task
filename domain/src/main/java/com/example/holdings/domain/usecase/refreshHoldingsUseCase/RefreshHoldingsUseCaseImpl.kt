package com.example.holdings.domain.usecase.refreshHoldingsUseCase

import com.example.holdings.core.Result
import com.example.holdings.domain.model.Holding
import com.example.holdings.domain.repository.HoldingsRepository

/**
 * Implementation of [RefreshHoldingsUseCase] that refreshes holdings data using the [HoldingsRepository].
 *
 * @param repository [HoldingsRepository] repository to refresh holdings data from.
 */
class RefreshHoldingsUseCaseImpl(
    private val repository: HoldingsRepository
) : RefreshHoldingsUseCase {
    override suspend operator fun invoke(): Result<List<Holding>> {
        val result = repository.fetchRemoteHoldings()
        if (result is Result.Success) {
            repository.saveHoldings(result.data)
        }
        return result
    }
}
