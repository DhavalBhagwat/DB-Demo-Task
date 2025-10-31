package com.example.holdings.data.repository

import com.example.holdings.core.Result
import com.example.holdings.data.dao.HoldingsDao
import com.example.holdings.data.model.HoldingDto
import com.example.holdings.data.model.HoldingEntity
import com.example.holdings.data.model.HoldingsData
import com.example.holdings.data.model.HoldingsResponse
import com.example.holdings.data.remote.HoldingsApi
import com.example.holdings.domain.model.Holding
import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.coVerifyOrder
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class HoldingsRepositoryImplTest {

    private lateinit var api: HoldingsApi
    private lateinit var dao: HoldingsDao
    private lateinit var repository: HoldingsRepositoryImpl

    @Before
    fun setUp() {
        api = mockk()
        dao = mockk()
        repository = HoldingsRepositoryImpl(api, dao)
    }

    @Test
    fun `fetchRemoteHoldings should return Success when API call is successful`() = runTest {
        // Arrange
        val mockResponse = HoldingsResponse(
            data = HoldingsData(
                userHolding = listOf(
                    HoldingDto(avgPrice = 50.0, close = 55.0, ltp = 60.0, quantity = 4, symbol = "ABC")
                )
            )
        )
        coEvery { api.fetchHoldings() } returns Result.Success(mockResponse)

        // Act
        val result = repository.fetchRemoteHoldings()

        // Assert
        assert(result is Result.Success)
        assertEquals(1, (result as Result.Success).data.size)
        assertEquals("ABC", result.data[0].symbol)
    }

    @Test(expected = Exception::class)
    fun `fetchRemoteHoldings should return Error when API call fails`() = runTest {
        // Arrange
        val exception = Exception("Network error")
        coEvery { api.fetchHoldings() } throws exception

        // Act
        val result = repository.fetchRemoteHoldings()

        // Assert
        assert(result is Result.Error)
        assertEquals("Failed to fetch holdings: Network error", (result as Result.Error).message)
    }

    @Test
    fun `getLocalHoldings should return mapped Flow of Holdings`() = runTest {
        // Arrange
        val mockEntities = listOf(
            HoldingEntity(symbol = "ABC", quantity = 4, ltp = 60.0, avgPrice = 50.0, close = 55.0)
        )
        every { dao.getHoldings() } returns flowOf(mockEntities)

        // Act
        val result = repository.getLocalHoldings()

        // Assert
        result.collect { holdings ->
            assertEquals(1, holdings.size)
            assertEquals("ABC", holdings[0].symbol)
        }
    }

    @Test
    fun `saveHoldings should clear and insert new holdings`() = runBlocking {
        // Arrange
        val holdings = listOf(
            Holding(symbol = "ABC", quantity = 4, ltp = 60.0, avgPrice = 50.0, close = 55.0)
        )
        coEvery { dao.clearAll() } just Runs
        coEvery { dao.insertHoldings(any()) } just Runs

        // Act
        repository.saveHoldings(holdings)

        // Assert
        coVerifyOrder {
            dao.clearAll()
            dao.insertHoldings(match { it.size == 1 && it[0].symbol == "ABC" })
        }
    }
}
