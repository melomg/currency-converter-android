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
package io.melih.android.currencyconverter.ui.currency.adapter

import android.os.Bundle
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import io.melih.android.currencyconverter.R
import io.melih.android.currencyconverter.ui.currency.DEFAULT_CURRENCY_AMOUNT
import io.melih.android.currencyconverter.ui.currency.CurrencyItemUIModel
import io.melih.android.currencyconverter.util.inflate
import java.math.BigDecimal

internal const val KEY_CURRENCY_VALUE = "KEY_CURRENCY_VALUE"
private const val VIEW_TYPE_ORIGINAL = 1
private const val VIEW_TYPE_CONVERTED = 2

class CurrencyRateListRecyclerAdapter(
    diffUtilItemCallback: DiffUtil.ItemCallback<CurrencyItemUIModel>,
    private val amountListener: (BigDecimal) -> Unit,
    private val itemClickListener: (CurrencyItemUIModel) -> Unit
) : ListAdapter<CurrencyItemUIModel, BaseConvertedCurrencyRateViewHolder>(diffUtilItemCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseConvertedCurrencyRateViewHolder =
        if (viewType == VIEW_TYPE_ORIGINAL) {
            OriginalCurrencyRateViewHolder(parent.inflate(R.layout.item_original_currency_rate), amountListener)
        } else {
            ConvertedCurrencyRateViewHolder(parent.inflate(R.layout.item_converted_currency_rate), itemClickListener)
        }

    override fun getItemViewType(position: Int): Int {
        return if (position == 0) {
            VIEW_TYPE_ORIGINAL
        } else {
            VIEW_TYPE_CONVERTED
        }
    }

    override fun onBindViewHolder(viewHolder: BaseConvertedCurrencyRateViewHolder, position: Int, payloads: MutableList<Any>) {
        if (payloads.isEmpty()) {
            super.onBindViewHolder(viewHolder, position, payloads)
            return
        }
        val bundle = payloads[0] as Bundle

        val currencyValue = try {
            bundle.getString(KEY_CURRENCY_VALUE)?.toBigDecimal() ?: DEFAULT_CURRENCY_AMOUNT
        } catch (exception: NumberFormatException) {
            DEFAULT_CURRENCY_AMOUNT
        }
        viewHolder.bindTo(position, currencyValue)
    }

    override fun onBindViewHolder(viewHolder: BaseConvertedCurrencyRateViewHolder, position: Int) {
        viewHolder.bindTo(position, getItem(position))
    }
}
