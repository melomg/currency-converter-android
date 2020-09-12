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

import android.os.Handler
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import java.math.BigDecimal
import kotlinx.android.synthetic.main.item_original_currency_rate.view.*

private const val SEARCH_DELAY_MS = 400L

class OriginalCurrencyRateViewHolder(
    itemView: View,
    listener: (BigDecimal) -> Unit
) : BaseConvertedCurrencyRateViewHolder(itemView) {

    private val textChangedListener = object : TextWatcher {
        private var priceRequestStartTime: Long = 0

        override fun afterTextChanged(s: Editable?) {
        }

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            // no-op
        }

        @Suppress("UNNECESSARY_SAFE_CALL")
        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            priceRequestStartTime = System.currentTimeMillis()

            if (adapterPosition != RecyclerView.NO_POSITION && adapterPosition == 0) {
                Handler().postDelayed({
                    itemView?.currencyAmountEditText?.priceValue?.let { onPriceEnteredWithDelay(it) }
                }, SEARCH_DELAY_MS)
            }
        }

        private fun onPriceEnteredWithDelay(priceValue: BigDecimal) {
            if (isUserPausedEnoughForQuery()) {
                listener(priceValue)
            }
        }

        private fun isUserPausedEnoughForQuery(): Boolean {
            val priceRequestPausedTime = System.currentTimeMillis()
            return ((priceRequestPausedTime - priceRequestStartTime) >= SEARCH_DELAY_MS)
        }
    }

    override fun View.bindCurrencyAmount(position: Int, currencyValue: BigDecimal) {
        currencyAmountEditText.priceValue = currencyValue

        currencyAmountEditText.isEnabled = true
        currencyAmountEditText.isFocusableInTouchMode = true
        currencyAmountEditText.setSelection(currencyAmountEditText.length())

        currencyAmountEditText.removeTextChangedListener(textChangedListener)
        currencyAmountEditText.addTextChangedListener(textChangedListener)
    }
}
