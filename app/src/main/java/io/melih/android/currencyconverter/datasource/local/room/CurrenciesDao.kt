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
import androidx.room.*
import io.melih.android.currencyconverter.datasource.local.room.model.CurrencyRoomModel
import io.melih.android.currencyconverter.model.Currency

@Suppress("unused")
@Dao
interface CurrenciesDao {
    @Query("SELECT * FROM currencies ORDER BY ordinal ASC")
    fun getAll(): LiveData<List<CurrencyRoomModel>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(currencies: List<CurrencyRoomModel>)

    @Transaction
    fun updateAllRates(currencies: List<Currency>) {
        currencies.forEach { updateCurrencyRate(it.currencyCode, it.rate.toString()) }
    }

    @Query("UPDATE currencies SET rate = :rate WHERE currencyCode = :currencyCode")
    fun updateCurrencyRate(currencyCode: String, rate: String)

    @Transaction
    fun updateAllOrdinals(currencies: List<Currency>) {
        currencies.forEach { currency ->
            currency.ordinal?.let { ordinal ->
                updateOrdinal(currency.currencyCode, ordinal)
            }
        }
    }

    @Query("UPDATE currencies SET ordinal = :ordinal WHERE currencyCode = :currencyCode")
    fun updateOrdinal(currencyCode: String, ordinal: Int)

    @Query("DELETE FROM currencies")
    fun deleteAll()
}
