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

import android.text.Editable
import android.text.TextWatcher
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import io.melih.android.currencyconverter.ui.PriceTextWatcher
import kotlinx.android.synthetic.main.item_currency_rate.view.*

class CurrencyRateViewHolder(
    itemView: View,
    listener: (String) -> Unit,
    private val itemClickListener: (CurrencyItemUIModel) -> Unit
) : RecyclerView.ViewHolder(itemView) {

    private val textChangedListener = object : TextWatcher {
        override fun afterTextChanged(s: Editable?) {
        }

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            // no-op
        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            if (adapterPosition != RecyclerView.NO_POSITION && adapterPosition == 0) {
                listener(s.toString())
            }
        }
    }

    fun bindTo(position: Int, toCurrency: CurrencyItemUIModel) = with(itemView) {
        setOnClickListener {
            if (adapterPosition != RecyclerView.NO_POSITION) {
                itemClickListener(toCurrency)
            }
        }
        currencyIcon.setImageDrawable(context.getDrawable(toCurrency.currencyDrawableResId))
        currencyCode.text = toCurrency.currencyCode
        currencyName.text = toCurrency.currencyName
        bindCurrencyAmount(position, toCurrency.currencyValue)
    }

    fun bindTo(position: Int, currencyValue: String) = with(itemView) {
        bindCurrencyAmount(position, currencyValue)
    }

    private fun View.bindCurrencyAmount(position: Int, currencyValue: String) {
        if (!currencyAmount.isSelected) currencyAmount.setText(currencyValue)

        if (position == 0) {
            currencyAmount.isEnabled = true
            currencyAmount.isFocusableInTouchMode = true
            currencyAmount.setSelection(currencyAmount.length())
            currencyAmount.removeTextChangedListener(textChangedListener)
            currencyAmount.addTextChangedListener(textChangedListener)
        } else {
            currencyAmount.isEnabled = false
            currencyAmount.isFocusableInTouchMode = false
        }

        currencyAmount.addTextChangedListener(PriceTextWatcher(currencyAmount))
    }
}
