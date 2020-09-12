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

import io.melih.android.currencyconverter.core.convertXCurrencyAmountToYCurrencyAmount
import io.melih.android.currencyconverter.core.model.Currency
import io.melih.android.currencyconverter.core.moveSelectedCurrencyToTop
import java.math.BigDecimal
import org.junit.Assert.assertEquals
import org.junit.Test

class CurrencyUtilsTest {

    @Test
    fun `convert X currency amount to Y currency amount`() {
        val euroCurrency = Currency("EUR", BigDecimal.ONE)
        val cadCurrency = Currency("CAD", BigDecimal.valueOf(1.5309))
        val cadCurrencyAmount = convertXCurrencyAmountToYCurrencyAmount(BigDecimal.valueOf(100), euroCurrency, cadCurrency)

        assertEquals(cadCurrencyAmount.toDouble(), BigDecimal.valueOf(153.09).toDouble(), 0.01)
    }

    @Test
    fun `move selected currency to top`() {
        val list = mutableListOf(
            Currency("EUR", BigDecimal.ONE),
            Currency("BGN", BigDecimal.valueOf(1.9521)),
            Currency("BRL", BigDecimal.valueOf(4.7827)),
            Currency("CAD", BigDecimal.valueOf(1.5309)),
            Currency("CHF", BigDecimal.valueOf(1.1254)),
            Currency("CNY", BigDecimal.valueOf(7.9301)),
            Currency("GBP", BigDecimal.valueOf(0.89654))
        )
        val selectedCurrencyCode = "GBP"
        moveSelectedCurrencyToTop(list) { it.currencyCode == selectedCurrencyCode }

        assertEquals(list[0].currencyCode, selectedCurrencyCode)
    }
}
