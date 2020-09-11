package io.melih.android.currencyconverter.localdatasource.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import io.melih.android.currencyconverter.localdatasource.CurrencyLocalDataSource
import io.melih.android.currencyconverter.localdatasource.room.CurrenciesDao
import io.melih.android.currencyconverter.localdatasource.room.CurrencyRoomDataSource
import javax.inject.Singleton

@Module
@InstallIn(ApplicationComponent::class)
class LocalDataSourceModule {

    @Singleton
    @Provides
    fun providesCurrencyLocalDataSource(currenciesDao: CurrenciesDao): CurrencyLocalDataSource = CurrencyRoomDataSource(currenciesDao)
}
