package com.example.holdings.data.di

import androidx.room.Room
import com.example.holdings.core.Constants
import com.example.holdings.data.db.AppDatabase
import com.example.holdings.data.remote.HoldingsApi
import com.example.holdings.data.remote.HoldingsApiImpl
import com.example.holdings.data.repository.HoldingsRepositoryImpl
import com.example.holdings.domain.repository.HoldingsRepository
import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import org.koin.dsl.module

/**
 * Koin module for providing data layer dependencies including
 */
val dataModule = module {

    // --- Database ---
    single {
        Room.databaseBuilder(get(), AppDatabase::class.java, Constants.DB_NAME)
            .fallbackToDestructiveMigration()
            .build()
    }

    single { get<AppDatabase>().holdingsDao() }

    // --- Network ---
    single {
        HttpClient(CIO) {
            install(ContentNegotiation) {
                json(
                    Json {
                        ignoreUnknownKeys = true
                    }
                )
            }
        }
    }

    single<HoldingsApi> { HoldingsApiImpl(get()) }

    // --- Repository ---
    single<HoldingsRepository> { HoldingsRepositoryImpl(get(), get()) }
}
