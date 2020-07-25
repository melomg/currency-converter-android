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
package io.melih.android.currencyconverter

import io.melih.android.currencyconverter.model.Currency
import io.melih.android.currencyconverter.ui.currency.CurrencyItemUIModel
import java.math.BigDecimal

/**
 * Test data for unit tests.
 */
object TestData {
    val CURRENCY_ITEM_UI_MODEL_LIST: List<CurrencyItemUIModel> = mutableListOf(
        CurrencyItemUIModel(R.drawable.ic_european_union, "EUR", "Euro", "1"),
        CurrencyItemUIModel(R.drawable.ic_australia, "AUD", "Australian Dollar", "1.6133"),
        CurrencyItemUIModel(R.drawable.ic_bulgaria, "BGN", "Bulgarian Lev", "1.9521"),
        CurrencyItemUIModel(R.drawable.ic_brazil, "BRL", "Brazilian Real", "4.7827"),
        CurrencyItemUIModel(R.drawable.ic_canada, "CAD", "Canadian Dollar", "1.5309")
    )

//    private val testDispatcher= TestCoroutineDispatcher()

    val CURRENCY_LIST: List<Currency> = mutableListOf(
        Currency("EUR", BigDecimal.valueOf(1)),
        Currency("AUD", BigDecimal.valueOf(1.6133)),
        Currency("BGN", BigDecimal.valueOf(1.9521)),
        Currency("BRL", BigDecimal.valueOf(4.7827)),
        Currency("CAD", BigDecimal.valueOf(1.5309))
    )
}
