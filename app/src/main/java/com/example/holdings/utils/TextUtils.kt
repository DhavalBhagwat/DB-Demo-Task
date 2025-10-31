package com.example.holdings.utils

import kotlin.math.abs

/**
 * Function to format the header value string.
 *
 * @param pnl [Double] profit or loss amount.
 * @param percent [percent] profit or loss percentage.
 * @return Formatted [String] for header display.
 */
internal fun formatHeaderValue(pnl: Double, percent: Double): String {
    val absPnl = abs(pnl)
    val sign = if (pnl >= 0) "+" else "−"
    return String.format("%s₹%,.2f (%s%.2f%%)", sign, absPnl, sign, abs(percent))
}

/**
 * Function to format currency values.
 *
 * @param value [Double] currency value.
 * @return Formatted currency [String].
 */
internal fun formatCurrency(value: Double): String {
    return "₹%,.2f".format(value)
}
