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
package io.melih.android.currencyconverter.repository

import io.melih.android.currencyconverter.core.CoroutineDispatcherProvider
import io.melih.android.currencyconverter.core.model.Currency
import io.melih.android.currencyconverter.core.model.Result
import io.melih.android.currencyconverter.core.moveSelectedCurrencyToTop
import io.melih.android.currencyconverter.localdatasource.CurrencyLocalDataSource
import io.melih.android.currencyconverter.remotedatasource.CurrencyRemoteDataSource
import java.util.Timer
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.concurrent.scheduleAtFixedRate
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import timber.log.Timber

private const val INTERVAL_IN_SECOND: Long = 30L
private val TAG: String = CurrencyRepository::class.java.simpleName

@Singleton
class CurrencyRepository @Inject constructor(
    private val localDataSource: CurrencyLocalDataSource,
    private val remoteDataSource: CurrencyRemoteDataSource,
    private val dispatcherProvider: CoroutineDispatcherProvider
) {
    private var timer: Timer? = null

    init {
        fetchAndSaveCurrencyRateList()
    }

    private fun fetchAndSaveCurrencyRateList() {
        timer = Timer(TAG, false).apply {
            scheduleAtFixedRate(0, TimeUnit.SECONDS.toMillis(INTERVAL_IN_SECOND)) {
                Timber.d("Timer is running ${System.currentTimeMillis()}")
                GlobalScope.launch(dispatcherProvider.io) {
                    when (val result = remoteDataSource.getLatestCurrencyRateList()) {
                        is Result.Success -> localDataSource.updateAllRates(result.data)
                    }
                }
            }
        }
    }

    fun getLatestCurrencyRateList(): Flow<Result<List<Currency>>> = localDataSource.getAllAsFlow()

    suspend fun updateAllOrdinals(selectedCurrencyCode: String) {
        val list = localDataSource.getAll().toMutableList()
        moveSelectedCurrencyToTop(list) { it.currencyCode == selectedCurrencyCode }
        list.forEachIndexed { index, currency ->
            currency.ordinal = index
        }
        localDataSource.updateAllOrdinals(list)
    }

    fun onClear() {
        timer?.cancel()
    }
}
