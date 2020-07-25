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
package io.melih.android.currencyconverter.ui.currency

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import io.melih.android.currencyconverter.R
import io.melih.android.currencyconverter.model.DEFAULT_CURRENCY_AMOUNT

internal const val KEY_CURRENCY_VALUE = "KEY_CURRENCY_VALUE"

class CurrencyRateListRecyclerAdapter(
    diffUtilItemCallback: DiffUtil.ItemCallback<CurrencyItemUIModel>,
    private val amountListener: (String) -> Unit,
    private val itemClickListener: (CurrencyItemUIModel) -> Unit
) : ListAdapter<CurrencyItemUIModel, CurrencyRateViewHolder>(diffUtilItemCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CurrencyRateViewHolder =
        CurrencyRateViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_currency_rate, parent, false),
            amountListener,
            itemClickListener
        )

    override fun onBindViewHolder(viewHolder: CurrencyRateViewHolder, position: Int, payloads: MutableList<Any>) {
        if (payloads.isEmpty()) {
            super.onBindViewHolder(viewHolder, position, payloads)
            return
        }
        val bundle = payloads[0] as Bundle
        val currencyValue = bundle.getString(KEY_CURRENCY_VALUE) ?: DEFAULT_CURRENCY_AMOUNT
        viewHolder.bindTo(position, currencyValue)
    }

    override fun onBindViewHolder(viewHolder: CurrencyRateViewHolder, position: Int) {
        viewHolder.bindTo(position, getItem(position))
    }
}
