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
package io.melih.android.currencyconverter.datasource.remote.retrofit

import io.melih.android.currencyconverter.datasource.remote.retrofit.response.ResponseCurrencyRateList
import kotlinx.coroutines.Deferred
import retrofit2.http.GET
import retrofit2.http.Query

private const val DEFAULT_BASE_CURRENCY = "EUR"

interface CurrencyApi {

    @GET("latest")
    fun getLatestCurrencyRateListAsync(@Query("base") baseCurrency: String = DEFAULT_BASE_CURRENCY): Deferred<ResponseCurrencyRateList>
}
