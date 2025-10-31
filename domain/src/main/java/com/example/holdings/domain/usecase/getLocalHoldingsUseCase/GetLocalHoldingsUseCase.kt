package com.example.holdings.domain.usecase.getLocalHoldingsUseCase

import com.example.holdings.domain.model.Holding
import kotlinx.coroutines.flow.Flow

/**
 * Use case interface for retrieving local financial holdings data.
 */
interface GetLocalHoldingsUseCase {

    /**
     * Invokes the use case to get local holdings data.
     *
     * @return [Flow] emitting a [List] of [Holding] objects.
     */
    operator fun invoke(): Flow<List<Holding>>
}
