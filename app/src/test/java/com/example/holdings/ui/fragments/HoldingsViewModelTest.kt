package com.example.holdings.ui.fragments

import com.example.holdings.core.Result
import com.example.holdings.domain.model.Holding
import com.example.holdings.domain.model.HoldingsSummary
import com.example.holdings.domain.usecase.computeHoldingsSummaryUseCase.ComputeHoldingsSummaryUseCase
import com.example.holdings.domain.usecase.getLocalHoldingsUseCase.GetLocalHoldingsUseCase
import com.example.holdings.domain.usecase.refreshHoldingsUseCase.RefreshHoldingsUseCase
import com.example.holdings.utils.NetworkUtils
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkObject
import io.mockk.unmockkAll
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class HoldingsViewModelTest {

    private val getLocalHoldingsUseCase = mockk<GetLocalHoldingsUseCase>()
    private val refreshHoldingsUseCase = mockk<RefreshHoldingsUseCase>()
    private val computeHoldingsSummaryUseCase = mockk<ComputeHoldingsSummaryUseCase>()

    private lateinit var viewModel: HoldingsViewModel

    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)

        // Mock NetworkUtils
        mockkObject(NetworkUtils)
        every { NetworkUtils.isNetworkAvailable() } returns true

        // Default mocks for use cases
        coEvery { getLocalHoldingsUseCase() } returns MutableStateFlow(emptyList())
        coEvery { refreshHoldingsUseCase() } returns Result.Success(emptyList())
        coEvery { computeHoldingsSummaryUseCase(any()) } returns HoldingsSummary(0.0, 0.0, 0.0)

        viewModel = HoldingsViewModel(
            getLocalHoldingsUseCase,
            refreshHoldingsUseCase,
            computeHoldingsSummaryUseCase
        )
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
        unmockkAll()
    }

    @Test
    fun `test refresh sets state to NoNetwork when network is unavailable`() = runTest {
        // Arrange
        every { NetworkUtils.isNetworkAvailable() } returns false

        // Act
        viewModel.refresh()
        delay(2000)

        // Assert
        assertEquals(Result.NoNetwork("No network connection"), viewModel.holdingsState.value)
    }

    @Test
    fun `test refresh sets state to Loading before fetching data`() = runTest {
        // Arrange
        coEvery { refreshHoldingsUseCase() } coAnswers {
            Result.Loading
        }

        // Act
        viewModel.refresh()
        testDispatcher.scheduler.advanceUntilIdle()

        // Assert
        assertEquals(Result.Loading, viewModel.holdingsState.value)
    }

    @Test
    fun `test refresh sets state to Success on successful data fetch`() = runTest {
        // Arrange
        val holdings = listOf(Holding("AAPL", 10, 150.0, 145.0, 148.0))
        coEvery { refreshHoldingsUseCase() } returns Result.Success(holdings)

        // Act
        viewModel.refresh()
        testDispatcher.scheduler.advanceUntilIdle()

        // Assert
        assertEquals(Result.Success(holdings), viewModel.holdingsState.value)
    }

    @Test
    fun `test refresh sets state to Error on failure`() = runTest {
        // Arrange
        coEvery { refreshHoldingsUseCase() } returns Result.Error("Error occurred")

        // Act
        viewModel.refresh()
        testDispatcher.scheduler.advanceUntilIdle()

        // Assert
        assertEquals(Result.Error("Error occurred"), viewModel.holdingsState.value)
    }

    @Test
    fun `test refresh sets state to Success with empty data`() = runTest {
        // Arrange
        coEvery { refreshHoldingsUseCase() } returns Result.Success(emptyList())

        // Act
        viewModel.refresh()
        testDispatcher.scheduler.advanceUntilIdle()

        // Assert
        assertEquals(Result.Success(emptyList<Holding>()), viewModel.holdingsState.value)
    }

    @Test
    fun `test observeLocalHoldings updates state and summary on non-empty list`() = runTest {
        // Arrange
        val holdings = listOf(Holding("AAPL", 10, 150.0, 145.0, 148.0))
        val holdingsFlow = MutableStateFlow(emptyList<Holding>())
        coEvery { getLocalHoldingsUseCase() } returns holdingsFlow
        coEvery { computeHoldingsSummaryUseCase(holdings) } returns HoldingsSummary(
            1000.0,
            5000.0,
            6000.0
        )
        viewModel = HoldingsViewModel(
            getLocalHoldingsUseCase,
            refreshHoldingsUseCase,
            computeHoldingsSummaryUseCase
        )

        // Act
        holdingsFlow.update { holdings }
        testDispatcher.scheduler.advanceUntilIdle()

        // Assert
        assertEquals(Result.Success(holdings), viewModel.holdingsState.value)
        assertEquals(HoldingsSummary(1000.0, 5000.0, 6000.0), viewModel.summary.value)
    }

    @Test
    fun `test observeLocalHoldings handles large holdings list`() = runTest {
        // Arrange
        val largeHoldings = List(1000) { Holding("SYM$it", it, 100.0, 90.0, 95.0) }
        val holdingsFlow = MutableStateFlow(emptyList<Holding>())
        coEvery { getLocalHoldingsUseCase() } returns holdingsFlow
        coEvery { computeHoldingsSummaryUseCase(largeHoldings) } returns HoldingsSummary(
            100000.0,
            90000.0,
            95000.0
        )
        viewModel = HoldingsViewModel(
            getLocalHoldingsUseCase,
            refreshHoldingsUseCase,
            computeHoldingsSummaryUseCase
        )

        // Act
        holdingsFlow.update { largeHoldings }
        testDispatcher.scheduler.advanceUntilIdle()

        // Assert
        assertEquals(Result.Success(largeHoldings), viewModel.holdingsState.value)
        assertEquals(HoldingsSummary(100000.0, 90000.0, 95000.0), viewModel.summary.value)
    }
}
