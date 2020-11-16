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
package io.melih.android.currencyconverter.repository

import io.melih.android.currencyconverter.core.model.Currency
import io.melih.android.currencyconverter.core.model.Result
import io.melih.android.currencyconverter.localdatasource.CurrencyLocalDataSource
import java.lang.reflect.Field
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

class FakeInMemoryCurrencyLocalDatasource : CurrencyLocalDataSource {

    private val currencies: MutableList<Currency> = mutableListOf()

    override fun getAllAsFlow(): Flow<Result<List<Currency>>> = flowOf(Result.Success(currencies.sortedBy { it.ordinal }))

    override suspend fun getAll(): List<Currency> = currencies.sortedBy { it.ordinal }

    override suspend fun updateAllRates(currencyList: List<Currency>) {
        currencies.forEach { currency ->
            currencyList.find { it.currencyCode == currency.currencyCode }?.rate?.let { currencyRate ->
                val field: Field = currency.javaClass.getDeclaredField("rate")
                field.isAccessible = true
                field.set(currency, currencyRate)
            }
        }
    }

    override suspend fun updateAllOrdinals(currencyList: List<Currency>) {
        currencies.forEach { currency ->
            currencyList.find { it.ordinal == currency.ordinal }?.ordinal?.let { currencyOrdinal ->
                currency.ordinal = currencyOrdinal
            }
        }
    }

    fun clearAndAddCurrencies(currencyList: List<Currency>) {
        clearCurrencies()
        currencies.addAll(currencyList)
    }

    fun clearCurrencies() {
        currencies.clear()
    }
}
