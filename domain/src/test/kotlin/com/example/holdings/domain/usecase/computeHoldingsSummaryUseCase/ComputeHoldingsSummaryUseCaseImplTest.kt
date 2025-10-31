package com.example.holdings.domain.usecase.computeHoldingsSummaryUseCase

import com.example.holdings.domain.model.Holding
import org.junit.Assert.assertEquals
import org.junit.Test

class ComputeHoldingsSummaryUseCaseImplTest {

    private val useCase = ComputeHoldingsSummaryUseCaseImpl()

    @Test
    fun `invoke should calculate correct summary for valid holdings`() {
        // Arrange
        val holdings = listOf(
            Holding(
                avgPrice = 50.0,
                close = 55.0,
                ltp = 60.0,
                quantity = 4,
                symbol = "ABC"
            ),
            Holding(
                avgPrice = 100.0,
                close = 110.0,
                ltp = 120.0,
                quantity = 3,
                symbol = "XYZ"
            )
        )
        val lastUpdatedMillis = 123456789L

        // Act
        val result = useCase.invoke(holdings, lastUpdatedMillis)

        // Assert
        assertEquals(600.0, result.totalCurrentValue, 0.0)
        assertEquals(500.0, result.totalInvestment, 0.0)
        assertEquals(100.0, result.totalPnl, 0.0)
        assertEquals(50.0, result.totalTodaysPnl, 0.0)
        assertEquals(20.0, result.totalPnlPercent, 0.01)
        assertEquals(lastUpdatedMillis, result.lastUpdatedMillis)
    }

    @Test
    fun `invoke should handle empty holdings list`() {
        // Arrange
        val holdings = emptyList<Holding>()
        val lastUpdatedMillis = null

        // Act
        val result = useCase.invoke(holdings, lastUpdatedMillis)

        // Assert
        assertEquals(0.0, result.totalCurrentValue, 0.0)
        assertEquals(0.0, result.totalInvestment, 0.0)
        assertEquals(0.0, result.totalPnl, 0.0)
        assertEquals(0.0, result.totalTodaysPnl, 0.0)
        assertEquals(0.0, result.totalPnlPercent, 0.0)
        assertEquals(lastUpdatedMillis, result.lastUpdatedMillis)
    }

    @Test
    fun `invoke should handle zero investment value`() {
        // Arrange
        val holdings = listOf(
            Holding(
                avgPrice = 0.0,
                close = 25.0,
                ltp = 25.0,
                quantity = 4,
                symbol = "DEF"
            )
        )
        val lastUpdatedMillis = 123456789L

        // Act
        val result = useCase.invoke(holdings, lastUpdatedMillis)

        // Assert
        assertEquals(100.0, result.totalCurrentValue, 0.0)
        assertEquals(0.0, result.totalInvestment, 0.0)
        assertEquals(100.0, result.totalPnl, 0.0)
        assertEquals(0.0, result.totalTodaysPnl, 0.0)
        assertEquals(0.0, result.totalPnlPercent, 0.0)
        assertEquals(lastUpdatedMillis, result.lastUpdatedMillis)
    }
}
