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

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.TypedArray
import androidx.annotation.ArrayRes
import androidx.annotation.DrawableRes
import io.melih.android.currencyconverter.R
import io.melih.android.currencyconverter.model.Currency
import io.melih.android.currencyconverter.util.convertXCurrencyAmountToYCurrencyAmount
import java.math.BigDecimal
import javax.inject.Singleton

@Singleton
class CurrencyDisplayableItemMapper(private val applicationContext: Context) {

    fun toCurrencyItemUIModelList(currencyList: List<Currency>?, amount: BigDecimal): List<CurrencyItemUIModel> {
        val list = currencyList?.toMutableList() ?: arrayListOf()
        return if (list.isNotEmpty()) {
            val xCurrency: Currency = list[0]
            val xCurrencyAmount: BigDecimal = amount

            list.map { yCurrency ->
                val yCurrencyAmount = convertXCurrencyAmountToYCurrencyAmount(xCurrencyAmount, xCurrency, yCurrency)
                toCurrencyItemUIModel(yCurrency, yCurrencyAmount)
            }
        } else emptyList()
    }

    @SuppressLint("ResourceType")
    private fun toCurrencyItemUIModel(yCurrency: Currency, yCurrencyAmount: BigDecimal): CurrencyItemUIModel {
        val country = Country.fromCurrencyCode(yCurrency.currencyCode)

        val countryTypedArray: TypedArray = applicationContext.resources.obtainTypedArray(country.currencyCodeResId)

        val currencyCode: String = countryTypedArray.getString(0) ?: throw IllegalStateException("country code is not valid")

        val currencyName: String = countryTypedArray.getString(1) ?: throw IllegalStateException("country name is not valid")

        @DrawableRes val currencyDrawableResId: Int = countryTypedArray.getResourceId(2, -1)

        countryTypedArray.recycle()

        return CurrencyItemUIModel(currencyDrawableResId, currencyCode, currencyName, yCurrencyAmount)
    }
}

@Suppress("unused")
private enum class Country(val currencyCode: String, @ArrayRes val currencyCodeResId: Int) {
    EUR("EUR", R.array.eur),
    AUD("AUD", R.array.aud),
    BGN("BGN", R.array.bgn),
    BRL("BRL", R.array.brl),
    CAD("CAD", R.array.cad),
    CHF("CHF", R.array.chf),
    CNY("CNY", R.array.cny),
    CZK("CZK", R.array.czk),
    DKK("DKK", R.array.dkk),
    GBP("GBP", R.array.gbp),
    HKD("HKD", R.array.hkd),
    HRK("HRK", R.array.hrk),
    HUF("HUF", R.array.huf),
    IDR("IDR", R.array.idr),
    ILS("ILS", R.array.ils),
    INR("INR", R.array.inr),
    ISK("ISK", R.array.isk),
    JPY("JPY", R.array.jpy),
    KRW("KRW", R.array.krw),
    MXN("MXN", R.array.mxn),
    MYR("MYR", R.array.myr),
    NOK("NOK", R.array.nok),
    NZD("NZD", R.array.nzd),
    PHP("PHP", R.array.php),
    PLN("PLN", R.array.pln),
    RON("RON", R.array.ron),
    RUB("RUB", R.array.rub),
    SEK("SEK", R.array.sek),
    SGD("SGD", R.array.sgd),
    THB("THB", R.array.thb),
    TRY("TRY", R.array.tr),
    USD("USD", R.array.usd),
    ZAR("ZAR", R.array.zar);

    companion object {

        private val map = values().associateBy(Country::currencyCode)

        fun fromCurrencyCode(currencyCode: String): Country = map[currencyCode] ?: throw IllegalArgumentException()
    }
}
