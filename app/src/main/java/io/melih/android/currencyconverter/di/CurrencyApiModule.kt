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

import com.facebook.stetho.okhttp3.StethoInterceptor
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import dagger.Module
import dagger.Provides
import io.melih.android.currencyconverter.BuildConfig
import io.melih.android.currencyconverter.datasource.remote.retrofit.CurrencyApi
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Named
import javax.inject.Singleton

private const val TIMEOUT_SECOND: Long = 15
private const val CURRENCY_API_TAG: String = "currency_api"

@Module
class CurrencyApiModule {

    @Singleton
    @Provides
    internal fun provideApi(@Named(CURRENCY_API_TAG) retrofit: Retrofit): CurrencyApi = retrofit.create(CurrencyApi::class.java)

    @Singleton
    @Provides
    @Named(CURRENCY_API_TAG)
    internal fun provideRetrofit(@Named(CURRENCY_API_TAG) okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BuildConfig.CURRENCY_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(CoroutineCallAdapterFactory())
            .client(okHttpClient)
            .build()
    }

    @Singleton
    @Provides
    @Named(CURRENCY_API_TAG)
    internal fun provideOkHttpClient(
        httpLoggingInterceptor: HttpLoggingInterceptor,
        @Named(CURRENCY_API_TAG) interceptors: Array<Interceptor>?,
        stethoInterceptor: StethoInterceptor?
    ): OkHttpClient {

        val httpClientBuilder = OkHttpClient.Builder()
        httpClientBuilder.connectTimeout(TIMEOUT_SECOND, TimeUnit.SECONDS)
            .readTimeout(TIMEOUT_SECOND, TimeUnit.SECONDS)
            .writeTimeout(TIMEOUT_SECOND, TimeUnit.SECONDS)

        if (stethoInterceptor != null) {
            httpClientBuilder.addNetworkInterceptor(stethoInterceptor)
        }

        for (interceptor in interceptors.orEmpty()) {
            httpClientBuilder.addInterceptor(interceptor)
        }

        httpLoggingInterceptor.level = if (BuildConfig.DEBUG) HttpLoggingInterceptor.Level.BODY else HttpLoggingInterceptor.Level.NONE
        httpClientBuilder.addInterceptor(httpLoggingInterceptor)

        return httpClientBuilder.build()
    }

    @Singleton
    @Provides
    @Named(CURRENCY_API_TAG)
    internal fun provideHttpInterceptors(): Array<Interceptor>? = null

    @Singleton
    @Provides
    internal fun provideHttpLoggingInterceptor(): HttpLoggingInterceptor = HttpLoggingInterceptor()

    @Singleton
    @Provides
    internal fun provideStethoInterceptor(): StethoInterceptor? = if (BuildConfig.DEBUG) StethoInterceptor() else null
}
