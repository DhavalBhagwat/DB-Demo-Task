package com.example.holdings.core.extensions

import com.example.holdings.core.Result

/**
 * Executes a suspending block of code and wraps the result in a [Result] object.
 *
 * @param block The suspending block of code to execute.
 * @return [Result] containing either the successful result or an error.
 */
suspend fun <T> safeCall(block: suspend () -> T): Result<T> {
    return try {
        Result.Success(block())
    } catch (t: Throwable) {
        Result.Error(t.message ?: "Unknown error", t)
    }
}
