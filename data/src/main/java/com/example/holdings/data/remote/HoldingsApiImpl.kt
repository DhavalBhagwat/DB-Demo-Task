package com.example.holdings.data.remote

import com.example.holdings.core.Constants
import com.example.holdings.core.Result
import com.example.holdings.core.extensions.safeCall
import com.example.holdings.data.model.HoldingsResponse
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.http.ContentType
import io.ktor.http.contentType

/**
 * Implementation of [HoldingsApi] using Ktor HTTP client to fetch holdings data from a remote API.
 *
 * @param client [HttpClient] Ktor HTTP client used for making network requests.
 */
class HoldingsApiImpl(
    private val client: HttpClient
) : HoldingsApi {

    override suspend fun fetchHoldings(): Result<HoldingsResponse> {
        return safeCall {
            val response = client.get(Constants.TEST_URL) {
                contentType(ContentType.Application.Json)
            }.body<HoldingsResponse>()

            response
        }
    }
}
