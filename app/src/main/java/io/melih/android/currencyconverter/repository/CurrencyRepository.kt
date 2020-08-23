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

import androidx.lifecycle.LiveData
import io.melih.android.currencyconverter.datasource.local.CurrencyLocalDataSource
import io.melih.android.currencyconverter.datasource.remote.CurrencyRemoteDataSource
import io.melih.android.currencyconverter.model.Currency
import io.melih.android.currencyconverter.model.Result
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import timber.log.Timber
import java.util.*
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.concurrent.scheduleAtFixedRate

private const val INTERVAL_IN_SECOND: Long = 30L
private val TAG: String = CurrencyRepository::class.java.simpleName

@Singleton
class CurrencyRepository @Inject constructor(
    private val localDataSource: CurrencyLocalDataSource,
    private val remoteDataSource: CurrencyRemoteDataSource
) {
    private var timer: Timer? = null

    fun getLatestCurrencyRateList(): LiveData<Result<List<Currency>>> {
        fetchAndSaveCurrencyRateList()
        return localDataSource.getAll()
    }

    private fun fetchAndSaveCurrencyRateList() {
        timer = Timer(TAG, false).apply {
            scheduleAtFixedRate(0, TimeUnit.SECONDS.toMillis(INTERVAL_IN_SECOND)) {
                Timber.d("timer is running ${System.currentTimeMillis()}")
                GlobalScope.launch {
                    when (val result = remoteDataSource.getLatestCurrencyRateList()) {
                        is Result.Success -> localDataSource.insertAll(result.data)
                    }
                }
            }
        }
    }

    fun onClear() {
        timer?.cancel()
    }
}
