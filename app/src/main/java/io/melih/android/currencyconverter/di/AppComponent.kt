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
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjector
import dagger.android.support.AndroidSupportInjectionModule
import io.melih.android.currencyconverter.ui.CurrencyConverterApplication
import io.melih.android.currencyconverter.ui.currency.di.CurrencyMapperModule
import io.melih.android.currencyconverter.ui.currency.di.CurrencyModule
import javax.inject.Singleton

@Singleton
@Component(
    modules = [
        AndroidSupportInjectionModule::class,
        CurrencyDatabaseModule::class,
        CurrencyApiModule::class,
        CoroutineModule::class,
        DataSourceModule::class,
        CurrencyModule::class,
        CurrencyMapperModule::class
    ]
)
interface AppComponent : AndroidInjector<CurrencyConverterApplication> {

    @Component.Factory
    interface Factory {
        fun create(@BindsInstance context: Context): AppComponent
    }
}
