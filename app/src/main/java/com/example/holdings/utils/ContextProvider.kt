package com.example.holdings.utils

import android.annotation.SuppressLint
import android.content.Context

/**
 * Singleton object to provide application context globally.
 *
 * Note: This done only as a part of a workaround for the sample project and should be used cautiously.
 * Ideally avoid using global context to prevent memory leaks.
 * Context should be passed explicitly where needed or injected via DI frameworks.
 */
@SuppressLint("StaticFieldLeak")
object ContextProvider {

    /** Instance of application [Context] */
    private lateinit var context: Context

    /**
     * Initialize the [ContextProvider] with the application [Context].
     *
     * @param context Application [Context] to initialize with.
     */
    fun initialize(context: Context) {
        this.context = context.applicationContext
    }

    /**
     * Get the application [Context].
     *
     * @throws IllegalStateException if [ContextProvider] is not initialized.
     */
    fun getContext(): Context {
        if (!::context.isInitialized) {
            throw IllegalStateException("ContextProvider is not initialized. Call initialize() first.")
        }
        return context
    }
}
