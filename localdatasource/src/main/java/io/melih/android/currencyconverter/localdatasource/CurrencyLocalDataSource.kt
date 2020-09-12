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
package io.melih.android.currencyconverter.localdatasource

import io.melih.android.currencyconverter.core.model.Currency
import io.melih.android.currencyconverter.core.model.Result
import kotlinx.coroutines.flow.Flow

interface CurrencyLocalDataSource {

    fun getAll(): Flow<Result<List<Currency>>>

    suspend fun updateAllRates(currencyList: List<Currency>)

    suspend fun updateAllOrdinals(currencyList: List<Currency>)
}
