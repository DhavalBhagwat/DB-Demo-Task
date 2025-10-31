package com.example.holdings

import android.app.Application
import com.example.holdings.data.di.dataModule
import com.example.holdings.di.appModule
import com.example.holdings.domain.di.domainModule
import com.example.holdings.utils.ContextProvider
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class HoldingsApp : Application() {
    override fun onCreate() {
        super.onCreate()
        ContextProvider.initialize(this)
        startKoin {
            androidContext(this@HoldingsApp)
            modules(
                dataModule,
                domainModule,
                appModule
            )
        }
    }
}
