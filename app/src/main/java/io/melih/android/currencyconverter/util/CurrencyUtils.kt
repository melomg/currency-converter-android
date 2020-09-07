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

import io.melih.android.currencyconverter.model.Currency
import java.math.BigDecimal

fun <T> moveSelectedCurrencyToTop(list: MutableList<T>, predicate: (T) -> Boolean): T? {
    val baseCurrency: T? = list.find(predicate)

    baseCurrency?.apply {
        list.remove(this)
        list.add(0, this)
    }

    return baseCurrency
}

fun convertXCurrencyAmountToYCurrencyAmount(xCurrencyAmount: BigDecimal, xCurrency: Currency, yCurrency: Currency): BigDecimal =
    if (xCurrency.rate == BigDecimal.ZERO) BigDecimal.ZERO else ((yCurrency.rate * xCurrencyAmount) / xCurrency.rate)
