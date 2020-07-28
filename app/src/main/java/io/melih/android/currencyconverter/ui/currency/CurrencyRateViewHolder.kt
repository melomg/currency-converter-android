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

import android.os.Handler
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.item_currency_rate.view.*
import org.threeten.bp.Duration
import org.threeten.bp.Instant
import java.math.BigDecimal

private const val SEARCH_DELAY_MS = 400L

class CurrencyRateViewHolder(
    itemView: View,
    listener: (BigDecimal) -> Unit,
    private val itemClickListener: (CurrencyItemUIModel) -> Unit
) : RecyclerView.ViewHolder(itemView) {

    private val textChangedListener = object : TextWatcher {
        private var searchRequestInstant: Instant? = null

        override fun afterTextChanged(s: Editable?) {
        }

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            // no-op
        }

        @Suppress("UNNECESSARY_SAFE_CALL")
        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            searchRequestInstant = Instant.now()

            if (adapterPosition != RecyclerView.NO_POSITION && adapterPosition == 0) {
                Handler().postDelayed({
                    itemView?.currencyAmount?.priceValue?.let { searchWithDelay(it) }
                }, SEARCH_DELAY_MS)
            }
        }

        private fun searchWithDelay(priceValue: BigDecimal) {
            val searchRequestPause = Instant.now()

            if (Duration.between(searchRequestInstant, searchRequestPause).toMillis() >= SEARCH_DELAY_MS) {
                listener(priceValue)
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
            currencyAmount.removeTextChangedListener(textChangedListener)
            currencyAmount.addTextChangedListener(textChangedListener)
        } else {
            currencyAmount.isEnabled = false
            currencyAmount.isFocusableInTouchMode = false
        }

    }
}
