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
package io.melih.android.currencyconverter.localdatasource.room

import io.melih.android.currencyconverter.core.exceptions.CurrenciesNotFound
import io.melih.android.currencyconverter.core.model.Currency
import io.melih.android.currencyconverter.core.model.Result
import io.melih.android.currencyconverter.localdatasource.CurrencyLocalDataSource
import io.melih.android.currencyconverter.localdatasource.room.model.toCurrencyList
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

@Singleton
class CurrencyRoomDataSource @Inject constructor(private val currenciesDao: CurrenciesDao) : CurrencyLocalDataSource {

    override fun getAll(): Flow<Result<List<Currency>>> = currenciesDao.getAll().map { currencyRoomModelList ->
        if (currencyRoomModelList.isNullOrEmpty()) return@map Result.Error(CurrenciesNotFound)

        return@map Result.Success(currencyRoomModelList.toCurrencyList())
    }

    override suspend fun updateAllRates(currencyList: List<Currency>) {
        currenciesDao.updateAllRates(currencyList)
    }

    override suspend fun updateAllOrdinals(currencyList: List<Currency>) {
        currenciesDao.updateAllOrdinals(currencyList)
    }
}
