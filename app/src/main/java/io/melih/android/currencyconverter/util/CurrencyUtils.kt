/*
 * Copyright 2019 CurrencyConverter
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.melih.android.currencyconverter.util

import timber.log.Timber
import java.math.BigDecimal
import java.text.DecimalFormat

private const val PRICE_FORMAT: String = "#,##0.00"
private val decimalFormat: DecimalFormat = DecimalFormat(PRICE_FORMAT).apply {
    minimumFractionDigits = 0
}

fun <T> moveSelectedCurrencyToTop(list: MutableList<T>, predicate: (T) -> Boolean): T? {
    val baseCurrency: T? = list.find(predicate)

    baseCurrency?.apply {
        list.remove(this)
        list.add(0, this)
    }

    return baseCurrency
}

fun BigDecimal.convertCurrencyAndFormat(baseCurrencyRate: BigDecimal, amount: BigDecimal): String =
    convertCurrency(baseCurrencyRate, amount).toString()

private fun BigDecimal.convertCurrency(baseCurrencyRate: BigDecimal, amount: BigDecimal): BigDecimal =
    try {
        ((this * amount) / baseCurrencyRate)
    } catch (e: NumberFormatException) {
        BigDecimal.ZERO
    }

fun formatCurrencyAmount(amount: String): String = try {
    val formatted = amount.replace("[^0-9.]".toRegex(), "")

    if (formatted.isEmpty()) "" else decimalFormat.format(
        java.lang.Double.parseDouble(formatted)
    )
} catch (e: NumberFormatException) {
    Timber.e(e)
    ""
}
