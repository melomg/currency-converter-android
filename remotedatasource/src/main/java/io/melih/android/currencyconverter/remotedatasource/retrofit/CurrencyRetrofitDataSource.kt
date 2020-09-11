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
package io.melih.android.currencyconverter.remotedatasource.retrofit

import io.melih.android.currencyconverter.model.Currency
import io.melih.android.currencyconverter.model.Result
import io.melih.android.currencyconverter.remotedatasource.CurrencyRemoteDataSource
import io.melih.android.currencyconverter.remotedatasource.safeApiCall
import java.io.IOException
import java.math.BigDecimal
import javax.inject.Inject
import javax.inject.Singleton

private const val UNKNOWN_ERROR = "Error fetching latest currency rates"

@Singleton
class CurrencyRetrofitDataSource @Inject constructor(private val api: CurrencyApi) : CurrencyRemoteDataSource {

    override suspend fun getLatestCurrencyRateList(): Result<List<Currency>> = safeApiCall(
        call = { requestLatestCurrencyRateList() },
        errorMessage = UNKNOWN_ERROR
    )

    private suspend fun requestLatestCurrencyRateList(): Result<List<Currency>> {
        val response = api.getLatestCurrencyRateListAsync().await()

        val ratesMap = response.ratesMap
        if (ratesMap.isNotEmpty()) {
            val currencyList = arrayListOf<Currency>()
            currencyList.add(Currency(response.baseCurrency, BigDecimal.ONE))

            currencyList.addAll(ratesMap.map { Currency(it.key, it.value) })

            return Result.Success(currencyList)
        }

        return Result.Error(IOException(UNKNOWN_ERROR))
    }
}
