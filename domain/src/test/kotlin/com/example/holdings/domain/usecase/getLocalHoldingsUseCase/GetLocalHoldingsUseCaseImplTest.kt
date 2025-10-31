package com.example.holdings.domain.usecase.getLocalHoldingsUseCase

import com.example.holdings.domain.model.Holding
import com.example.holdings.domain.repository.HoldingsRepository
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class GetLocalHoldingsUseCaseImplTest {

    private lateinit var repository: HoldingsRepository
    private lateinit var useCase: GetLocalHoldingsUseCaseImpl

    @Before
    fun setUp() {
        repository = mockk()
        useCase = GetLocalHoldingsUseCaseImpl(repository)
    }

    @Test
    fun `invoke should call getLocalHoldings on repository`() = runTest {
        // Arrange
        val holdings = listOf(
            Holding(avgPrice = 50.0, close = 55.0, ltp = 60.0, quantity = 4, symbol = "ABC"),
            Holding(avgPrice = 100.0, close = 110.0, ltp = 120.0, quantity = 3, symbol = "XYZ")
        )
        coEvery { repository.getLocalHoldings() } returns flowOf(holdings)

        // Act
        val result: Flow<List<Holding>> = useCase.invoke()

        // Assert
        result.collect { emittedHoldings ->
            assertEquals(holdings, emittedHoldings)
        }
    }

    @Test
    fun `invoke should emit empty list when repository returns no data`() = runTest {
        // Arrange
        coEvery { repository.getLocalHoldings() } returns flowOf(emptyList())

        // Act
        val result: Flow<List<Holding>> = useCase.invoke()

        // Assert
        result.collect { emittedHoldings ->
            assertEquals(emptyList<Holding>(), emittedHoldings)
        }
    }
}
