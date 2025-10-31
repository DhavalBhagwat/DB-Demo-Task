package com.example.holdings.domain.usecase.refreshHoldingsUseCase

import com.example.holdings.core.Result
import com.example.holdings.domain.model.Holding
import com.example.holdings.domain.repository.HoldingsRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class RefreshHoldingsUseCaseImplTest {

    private lateinit var repository: HoldingsRepository
    private lateinit var useCase: RefreshHoldingsUseCaseImpl

    @Before
    fun setUp() {
        repository = mockk(relaxed = true)
        useCase = RefreshHoldingsUseCaseImpl(repository)
    }

    @Test
    fun `invoke should call fetchRemoteHoldings on repository`() = runTest {
        // Arrange
        coEvery { repository.fetchRemoteHoldings() } returns Result.Success(emptyList())

        // Act
        useCase.invoke()

        // Assert
        coVerify { repository.fetchRemoteHoldings() }
    }

    @Test
    fun `invoke should call saveHoldings when fetchRemoteHoldings is successful`() = runTest {
        // Arrange
        val holdings = listOf(
            Holding(avgPrice = 50.0, close = 55.0, ltp = 60.0, quantity = 4, symbol = "ABC")
        )
        coEvery { repository.fetchRemoteHoldings() } returns Result.Success(holdings)

        // Act
        useCase.invoke()

        // Assert
        coVerify { repository.saveHoldings(holdings) }
    }

    @Test
    fun `invoke should not call saveHoldings when fetchRemoteHoldings fails`() = runTest {
        // Arrange
        coEvery { repository.fetchRemoteHoldings() } returns Result.Error("Network error")

        // Act
        useCase.invoke()

        // Assert
        coVerify(exactly = 0) { repository.saveHoldings(any()) }
    }

    @Test
    fun `invoke should return the same result as fetchRemoteHoldings`() = runTest {
        // Arrange
        val expectedResult = Result.Error("Network error")
        coEvery { repository.fetchRemoteHoldings() } returns expectedResult

        // Act
        val result = useCase.invoke()

        // Assert
        assertEquals(expectedResult, result)
    }
}
