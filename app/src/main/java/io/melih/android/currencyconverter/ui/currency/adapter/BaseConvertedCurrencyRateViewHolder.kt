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
package io.melih.android.currencyconverter.ui.currency.adapter

import android.view.View
import androidx.annotation.CallSuper
import androidx.recyclerview.widget.RecyclerView
import io.melih.android.currencyconverter.ui.currency.CurrencyItemUIModel
import java.math.BigDecimal
import kotlinx.android.synthetic.main.layout_base_item_currency_rate.view.*

abstract class BaseConvertedCurrencyRateViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    @CallSuper
    open fun bindTo(position: Int, currencyItemUIModel: CurrencyItemUIModel) = with(itemView) {
        currencyIcon.setImageDrawable(context.getDrawable(currencyItemUIModel.currencyDrawableResId))
        currencyCode.text = currencyItemUIModel.currencyCode
        currencyName.text = currencyItemUIModel.currencyName
        bindCurrencyAmount(position, currencyItemUIModel.currencyValue)
    }

    @CallSuper
    open fun bindTo(position: Int, currencyValue: BigDecimal) = with(itemView) {
        bindCurrencyAmount(position, currencyValue)
    }

    abstract fun View.bindCurrencyAmount(position: Int, currencyValue: BigDecimal)
}
