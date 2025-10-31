package com.example.holdings.domain.di

import com.example.holdings.domain.usecase.computeHoldingsSummaryUseCase.ComputeHoldingsSummaryUseCase
import com.example.holdings.domain.usecase.computeHoldingsSummaryUseCase.ComputeHoldingsSummaryUseCaseImpl
import com.example.holdings.domain.usecase.getLocalHoldingsUseCase.GetLocalHoldingsUseCase
import com.example.holdings.domain.usecase.getLocalHoldingsUseCase.GetLocalHoldingsUseCaseImpl
import com.example.holdings.domain.usecase.refreshHoldingsUseCase.RefreshHoldingsUseCase
import com.example.holdings.domain.usecase.refreshHoldingsUseCase.RefreshHoldingsUseCaseImpl
import org.koin.dsl.module

/**
 * Koin module for providing domain layer dependencies including use cases.
 */
val domainModule = module {
    factory<GetLocalHoldingsUseCase> { GetLocalHoldingsUseCaseImpl(get()) }
    factory<RefreshHoldingsUseCase> { RefreshHoldingsUseCaseImpl(get()) }
    factory<ComputeHoldingsSummaryUseCase> { ComputeHoldingsSummaryUseCaseImpl() }
}
