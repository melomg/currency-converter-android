/*
 * Copyright 2020 CurrencyConverter
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
package io.melih.android.currencyconverter.unittestshared

import io.melih.android.currencyconverter.core.model.Currency
import java.math.BigDecimal

val UPDATED_CURRENCY_LIST: List<Currency> = mutableListOf(
    Currency("EUR", BigDecimal.valueOf(1), 0),
    Currency("AUD", BigDecimal.valueOf(1.7233), 1),
    Currency("BGN", BigDecimal.valueOf(1.851), 2),
    Currency("BRL", BigDecimal.valueOf(4.887), 3),
    Currency("CAD", BigDecimal.valueOf(1.43), 4)
)

val CURRENCY_LIST: List<Currency> = listOf(
    Currency("EUR", BigDecimal.valueOf(1), 0),
    Currency("AUD", BigDecimal.valueOf(1.6133), 1),
    Currency("BGN", BigDecimal.valueOf(1.9521), 2),
    Currency("BRL", BigDecimal.valueOf(4.7827), 3),
    Currency("CAD", BigDecimal.valueOf(1.5309), 4)
)
