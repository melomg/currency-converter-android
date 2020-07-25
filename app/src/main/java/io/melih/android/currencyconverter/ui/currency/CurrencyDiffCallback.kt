/*
 * Copyright 2020 CurrencyConverter
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
package io.melih.android.currencyconverter.ui.currency

import android.os.Bundle
import androidx.recyclerview.widget.DiffUtil

object CurrencyDiffCallback : DiffUtil.ItemCallback<CurrencyItemUIModel>() {
    override fun areItemsTheSame(oldItem: CurrencyItemUIModel, newItem: CurrencyItemUIModel): Boolean =
        (oldItem.currencyCode == newItem.currencyCode)

    override fun areContentsTheSame(oldItem: CurrencyItemUIModel, newItem: CurrencyItemUIModel): Boolean =
        (oldItem == newItem)

    override fun getChangePayload(oldItem: CurrencyItemUIModel, newItem: CurrencyItemUIModel): Any? {
        val diffBundle = Bundle()
        if (newItem.currencyValue !== oldItem.currencyValue) {
            diffBundle.putString(KEY_CURRENCY_VALUE, newItem.currencyValue)
        }

        return if (diffBundle.size() == 0) null else diffBundle
    }
}
