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
import org.junit.Assert.assertEquals
import org.junit.Test
import java.math.BigDecimal

class CurrencyUtilsTest {

    @Test
    fun convertCurrencyAndFormat_with_correct_amount() {
        val baseCurrencyRate = BigDecimal(1)
        val currencyRate = BigDecimal.valueOf(1.4)
        val amount = "1,000"
        val convertedCurrencyValue = currencyRate.convertCurrencyAndFormat(baseCurrencyRate, amount)

        assertEquals(convertedCurrencyValue, "1400.0")
    }

    @Test
    fun convertCurrencyAndFormat_with_correct_amount_with_floating_point() {
        val baseCurrencyRate = BigDecimal(1)
        val currencyRate = BigDecimal.valueOf(1.4)
        val amount = "1,000.25"
        val convertedCurrencyValue = currencyRate.convertCurrencyAndFormat(baseCurrencyRate, amount)

        assertEquals(convertedCurrencyValue, "1400.350")
    }

    @Test
    fun convertCurrencyAndFormat_with_wrong_amount() {
        val baseCurrencyRate = BigDecimal(1)
        val currencyRate = BigDecimal.valueOf(1.4)
        val amount = "1.00.0"
        val convertedCurrencyValue = currencyRate.convertCurrencyAndFormat(baseCurrencyRate, amount)

        assertEquals(convertedCurrencyValue, "0")
    }

    @Test
    fun formatCurrencyAmount_with_correct_amount() {
        val currencyAmount = "10500"
        val formattedCurrencyAmount =
            formatCurrencyAmount(currencyAmount)

        assertEquals(formattedCurrencyAmount, "10,500")
    }

    @Test
    fun formatCurrencyAmount_removes_unnecessary_fraction_digits() {
        val currencyAmount = "10.500"
        val formattedCurrencyAmount =
            formatCurrencyAmount(currencyAmount)

        assertEquals(formattedCurrencyAmount, "10.5")
    }

    @Test
    fun formatCurrencyAmount_with_wrong_amount() {
        val currencyAmount = "10.500.4"
        val formattedCurrencyAmount =
            formatCurrencyAmount(currencyAmount)

        assertEquals(formattedCurrencyAmount, "")
    }

    @Test
    fun moveSelectedCurrencyToTop() {
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
