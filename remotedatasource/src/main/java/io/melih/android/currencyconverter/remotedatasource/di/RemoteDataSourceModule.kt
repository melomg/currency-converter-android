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
package io.melih.android.currencyconverter.remotedatasource.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import io.melih.android.currencyconverter.remotedatasource.CurrencyRemoteDataSource
import io.melih.android.currencyconverter.remotedatasource.retrofit.CurrencyApi
import io.melih.android.currencyconverter.remotedatasource.retrofit.CurrencyRetrofitDataSource

@Module
@InstallIn(ApplicationComponent::class)
class RemoteDataSourceModule {

    @Provides
    fun providesCurrencyRemoteDataSource(api: CurrencyApi): CurrencyRemoteDataSource = CurrencyRetrofitDataSource(api)
}
