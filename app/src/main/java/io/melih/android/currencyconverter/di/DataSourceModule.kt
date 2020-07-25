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
package io.melih.android.currencyconverter.di

import dagger.Module
import dagger.Provides
import io.melih.android.currencyconverter.datasource.local.CurrencyLocalDataSource
import io.melih.android.currencyconverter.datasource.local.room.CurrenciesDao
import io.melih.android.currencyconverter.datasource.local.room.CurrencyRoomDataSource
import io.melih.android.currencyconverter.datasource.remote.CurrencyRemoteDataSource
import io.melih.android.currencyconverter.datasource.remote.retrofit.CurrencyApi
import io.melih.android.currencyconverter.datasource.remote.retrofit.CurrencyRetrofitDataSource

@Module
class DataSourceModule {

    @Provides
    fun providesCurrencyLocalDataSource(currenciesDao: CurrenciesDao): CurrencyLocalDataSource = CurrencyRoomDataSource(currenciesDao)

    @Provides
    fun providesCurrencyRemoteDataSource(api: CurrencyApi): CurrencyRemoteDataSource = CurrencyRetrofitDataSource(api)
}
