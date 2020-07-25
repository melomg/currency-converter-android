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

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import io.melih.android.currencyconverter.datasource.local.room.AppDatabase
import io.melih.android.currencyconverter.datasource.local.room.CurrenciesDao
import javax.inject.Singleton

private const val APP_DB_NAME: String = "currency.db"

@Module
class CurrencyDatabaseModule {

    @Singleton
    @Provides
    internal fun provideCurrencyDao(appDB: AppDatabase): CurrenciesDao = appDB.currenciesDao()

    @Singleton
    @Provides
    internal fun provideDatabase(context: Context): AppDatabase {
        return Room.databaseBuilder(context.applicationContext, AppDatabase::class.java, APP_DB_NAME)
            .fallbackToDestructiveMigration()
            .build()
    }
}
