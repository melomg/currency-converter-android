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

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import io.melih.android.currencyconverter.ui.currency.CurrencyItemUIModel
import kotlinx.android.synthetic.main.item_converted_currency_rate.view.*
import melih.android.customviews.decimalFormat
import java.math.BigDecimal

class ConvertedCurrencyRateViewHolder(
    itemView: View,
    private val itemClickListener: (CurrencyItemUIModel) -> Unit
) : BaseConvertedCurrencyRateViewHolder(itemView) {

    override fun bindTo(position: Int, currencyItemUIModel: CurrencyItemUIModel) = with(itemView) {
        super.bindTo(position, currencyItemUIModel)

        setOnClickListener {
            if (adapterPosition != RecyclerView.NO_POSITION) {
                itemClickListener(currencyItemUIModel)
            }
        }
    }

    override fun View.bindCurrencyAmount(position: Int, currencyValue: BigDecimal) {
        currencyAmountTextView.text = decimalFormat.format(currencyValue.toDouble())
    }

}
