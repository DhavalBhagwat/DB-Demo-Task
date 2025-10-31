package com.example.holdings.di

import com.example.holdings.ui.fragments.HoldingsViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

/**
 * Koin module to provide app dependencies.
 */
val appModule = module {
    viewModel { HoldingsViewModel(get(), get(), get()) }
}
