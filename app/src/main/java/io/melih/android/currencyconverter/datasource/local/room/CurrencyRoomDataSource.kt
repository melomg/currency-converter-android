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
package io.melih.android.currencyconverter.datasource.local.room

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import io.melih.android.currencyconverter.datasource.local.CurrenciesNotFound
import io.melih.android.currencyconverter.datasource.local.CurrencyLocalDataSource
import io.melih.android.currencyconverter.datasource.local.room.model.toCurrencyList
import io.melih.android.currencyconverter.datasource.local.room.model.toCurrencyRoomModelList
import io.melih.android.currencyconverter.model.Currency
import io.melih.android.currencyconverter.model.Result
import javax.inject.Inject

class CurrencyRoomDataSource @Inject constructor(private val currenciesDao: CurrenciesDao) : CurrencyLocalDataSource {

    override fun getAll(): LiveData<Result<List<Currency>>> = Transformations.map(currenciesDao.getAll()) { currencyRoomModelList ->
        if (currencyRoomModelList.isNullOrEmpty()) return@map Result.Error(CurrenciesNotFound)

        return@map Result.Success(currencyRoomModelList.toCurrencyList())
    }

    override suspend fun insertAll(currencyList: List<Currency>) {
        currenciesDao.insertAll(currencyList.toCurrencyRoomModelList())
    }
}
