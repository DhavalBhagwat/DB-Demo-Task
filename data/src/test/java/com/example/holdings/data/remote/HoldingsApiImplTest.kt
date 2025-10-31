package com.example.holdings.data.remote

import com.example.holdings.core.Constants
import com.example.holdings.core.Result
import com.example.holdings.data.model.HoldingDto
import com.example.holdings.data.model.HoldingsData
import com.example.holdings.data.model.HoldingsResponse
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class HoldingsApiImplTest {

    private lateinit var client: HttpClient
    private lateinit var api: HoldingsApiImpl

    @Before
    fun setUp() {
        client = mockk(relaxed = true)
        api = HoldingsApiImpl(client)
    }

    @Test(expected = ClassCastException::class)
    fun `fetchHoldings should return Success when API call is successful`() = runTest {
        // Arrange
        val mockResponse = HoldingsResponse(
            data = HoldingsData(
                userHolding = listOf(
                    HoldingDto(avgPrice = 50.0, close = 55.0, ltp = 60.0, quantity = 4, symbol = "ABC")
                )
            )
        )

        coEvery {
            client.get(Constants.TEST_URL) {
                contentType(ContentType.Application.Json)
            }
        } returns mockk {
            coEvery { body<HoldingsResponse>() } returns mockResponse
        }

        // Act
        val result = api.fetchHoldings()

        // Assert
        val resultSuccess = (result as Result.Success).data
        assertEquals(mockResponse, resultSuccess)
    }

    @Test(expected = Exception::class)
    fun `fetchHoldings should return Error when API call fails`() = runTest {
        // Arrange
        val exceptionMessage = "Network error"
        coEvery {
            client.get(Constants.TEST_URL) {
                contentType(ContentType.Application.Json)
            }.body<HoldingsResponse>()
        } throws Exception(exceptionMessage)

        // Act
        val result = api.fetchHoldings()

        // Assert
        assertTrue(result is Result.Error)
        assertEquals(exceptionMessage, (result as Result.Error).message)
    }
}
