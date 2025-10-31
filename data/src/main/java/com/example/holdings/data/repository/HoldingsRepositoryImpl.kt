package com.example.holdings.data.repository

import com.example.holdings.core.Result
import com.example.holdings.data.dao.HoldingsDao
import com.example.holdings.data.model.HoldingEntity
import com.example.holdings.data.remote.HoldingsApi
import com.example.holdings.domain.model.Holding
import com.example.holdings.domain.repository.HoldingsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

/**
 * Implementation of [HoldingsRepository] that manages financial holdings data.
 *
 * @property api [HoldingsApi] remote API for fetching holdings data.
 * @property dao [HoldingsDao] local DAO for accessing cached holdings data.
 */
class HoldingsRepositoryImpl(
    private val api: HoldingsApi,
    private val dao: HoldingsDao
) : HoldingsRepository {

    override fun getLocalHoldings(): Flow<List<Holding>> {
        return dao.getHoldings().map { list ->
            list.map { it.toDomain() }
        }
    }

    override suspend fun fetchRemoteHoldings(): Result<List<Holding>> {
        return when (val result = api.fetchHoldings()) {
            is Result.Success -> {
                try {
                    val holdings = result.data.data.userHolding.map {
                        Holding(
                            symbol = it.symbol,
                            quantity = it.quantity,
                            ltp = it.ltp,
                            avgPrice = it.avgPrice,
                            close = it.close
                        )
                    }
                    Result.Success(holdings)
                } catch (e: Exception) {
                    Result.Error("Failed to process holdings: ${e.message}", e)
                }
            }

            is Result.Error -> Result.Error(result.message, result.cause)
            is Result.NoNetwork -> Result.NoNetwork(result.message)
            else -> Result.Error("Unknown error occurred")
        }
    }

    override suspend fun saveHoldings(holdings: List<Holding>) {
        dao.clearAll()
        dao.insertHoldings(holdings.map { it.toEntity() })
    }

    /**
     * Extension function to convert [HoldingEntity] to [Holding] domain model.
     */
    private fun HoldingEntity.toDomain() = Holding(symbol, quantity, ltp, avgPrice, close)

    /**
     * Extension function to convert [Holding] domain model to [HoldingEntity].
     */
    private fun Holding.toEntity() = HoldingEntity(symbol, quantity, ltp, avgPrice, close)
}
