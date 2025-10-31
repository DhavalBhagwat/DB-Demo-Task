package com.example.holdings.core

/**
 * Result wrapper for clean error handling across layers.
 */
sealed class Result<out T> {
    /** Loading state */
    data object Loading : Result<Nothing>()

    /** Success result with data */
    data class Success<T>(val data: T) : Result<T>()

    /** Error result with message and optional cause */
    data class Error(val message: String, val cause: Throwable? = null) : Result<Nothing>()

    /** No network connectivity result with message */
    data class NoNetwork(val message: String) : Result<Nothing>()
}
