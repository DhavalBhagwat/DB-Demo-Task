package com.example.holdings.ui.fragments

import androidx.annotation.VisibleForTesting
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.holdings.core.Result
import com.example.holdings.domain.model.Holding
import com.example.holdings.domain.model.HoldingsSummary
import com.example.holdings.domain.usecase.computeHoldingsSummaryUseCase.ComputeHoldingsSummaryUseCase
import com.example.holdings.domain.usecase.getLocalHoldingsUseCase.GetLocalHoldingsUseCase
import com.example.holdings.domain.usecase.refreshHoldingsUseCase.RefreshHoldingsUseCase
import com.example.holdings.utils.NetworkUtils
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

/**
 * ViewModel for managing financial holdings data.
 *
 * @param getLocalHoldingsUseCase [GetLocalHoldingsUseCase] use case to get local holdings.
 * @param refreshHoldingsUseCase [RefreshHoldingsUseCase] use case to refresh holdings data.
 * @param computeHoldingsSummaryUseCase [ComputeHoldingsSummaryUseCase] use case to compute holdings summary.
 */
class HoldingsViewModel(
    private val getLocalHoldingsUseCase: GetLocalHoldingsUseCase,
    private val refreshHoldingsUseCase: RefreshHoldingsUseCase,
    private val computeHoldingsSummaryUseCase: ComputeHoldingsSummaryUseCase
) : ViewModel() {

    /** [StateFlow] holding the current state of holdings data. */
    private val _holdingsState = MutableStateFlow<Result<List<Holding>>>(Result.Loading)

    /** Publicly exposed [StateFlow] for holdings data state. */
    val holdingsState: StateFlow<Result<List<Holding>>> = _holdingsState

    /** [StateFlow] holding the summary of holdings. */
    private val _summary = MutableStateFlow(HoldingsSummary())

    /** Publicly exposed [StateFlow] for holdings summary. */
    val summary: StateFlow<HoldingsSummary> = _summary

    init {
        observeLocalHoldings()
        refresh()
    }

    /**
     * Function to observe local holdings data and updates the state accordingly.
     */
    @VisibleForTesting
    internal fun observeLocalHoldings() {
        viewModelScope.launch {
            getLocalHoldingsUseCase().catch {
                _holdingsState.value = Result.Error(it.message ?: "An error occurred")
                return@catch
            }.collectLatest { list ->
                if (list.isNotEmpty()) {
                    _holdingsState.value = Result.Success(list)
                    _summary.value = computeHoldingsSummaryUseCase(list)
                }
            }
        }
    }

    /**
     * Function to refresh the holdings data by invoking the refresh use case.
     */
    @VisibleForTesting
    internal fun refresh() {
        viewModelScope.launch {
            if (!NetworkUtils.isNetworkAvailable()) {
                _holdingsState.value = Result.NoNetwork("No network connection")
                return@launch
            }

            _holdingsState.value = Result.Loading
            val result = refreshHoldingsUseCase()
            _holdingsState.value = result
        }
    }
}
