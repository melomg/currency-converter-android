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
package io.melih.android.currencyconverter.datasource.local.room.model

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import io.melih.android.currencyconverter.model.Currency

@Entity(tableName = "currencies", indices = [Index(value = ["ordinal"])])
data class CurrencyRoomModel(
    @PrimaryKey
    @SerializedName("currency_code")
    val currencyCode: String,

    @SerializedName("rate")
    var rate: String,

    @SerializedName("ordinal")
    var ordinal: Int

)

fun CurrencyRoomModel.toCurrency(): Currency = Currency(currencyCode, rate.toBigDecimal(), ordinal)

fun List<CurrencyRoomModel>?.toCurrencyList(): List<Currency> {
    if (this == null) return emptyList()

    return map { it.toCurrency() }
}

fun Currency.toCurrencyRoomModel(): CurrencyRoomModel = CurrencyRoomModel(currencyCode, rate.toString(), 1)

fun List<Currency>?.toCurrencyRoomModelList(): List<CurrencyRoomModel> {
    if (this == null) return emptyList()

    return map { it.toCurrencyRoomModel() }
}
