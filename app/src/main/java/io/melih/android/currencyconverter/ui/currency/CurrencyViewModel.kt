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

import androidx.annotation.UiThread
import androidx.annotation.VisibleForTesting
import androidx.lifecycle.*
import io.melih.android.currencyconverter.di.ActivityScope
import io.melih.android.currencyconverter.model.Currency
import io.melih.android.currencyconverter.model.DEFAULT_CURRENCY_AMOUNT
import io.melih.android.currencyconverter.model.DEFAULT_CURRENCY_CODE
import io.melih.android.currencyconverter.model.Result
import io.melih.android.currencyconverter.repository.CurrencyRepository
import io.melih.android.currencyconverter.util.event.Event
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.launch
import java.math.BigDecimal
import javax.inject.Inject

@ActivityScope
class CurrencyViewModel @Inject constructor(
    private val currencyDisplayableItemMapper: CurrencyDisplayableItemMapper,
    private val defaultDispatcher: CoroutineDispatcher,
    private val currencyRepository: CurrencyRepository
) : ViewModel() {

    private val _currencyItemUIModelList = MediatorLiveData<List<CurrencyItemUIModel>>()
    val currencyItemUIModelList: LiveData<List<CurrencyItemUIModel>>
        get() = _currencyItemUIModelList

    private val _errorLiveData = MutableLiveData<Event<Exception>>()
    val errorLiveData: LiveData<Event<Exception>>
        get() = _errorLiveData

    @VisibleForTesting
    var selectedCurrencyCode: String? = null
    private val amountLiveData = MutableLiveData<BigDecimal>()
    private val currencyListLiveData = MediatorLiveData<List<Currency>>()

    init {
        changeCurrencyCode(DEFAULT_CURRENCY_CODE)
        setAmount(DEFAULT_CURRENCY_AMOUNT)
        _currencyItemUIModelList.addSource(currencyListLiveData) { list ->
            val currencyCode = selectedCurrencyCode
            val amount = amountLiveData.value
            if (currencyCode != null && amount != null && list != null) {
                onCurrencyListChanged(amount, list)
            }
        }
        currencyListLiveData.addSource(currencyRepository.getLatestCurrencyRateList()) { result ->
            when (result) {
                is Result.Error -> _errorLiveData.value = Event(result.exception)
                is Result.Success -> currencyListLiveData.value = result.data
            }
        }
        currencyListLiveData.addSource(amountLiveData) { amount ->
            val currencyCode = selectedCurrencyCode
            val currencyList = currencyListLiveData.value
            if (currencyCode != null && currencyList != null) {
                onCurrencyListChanged(amount, currencyList)
            }
        }
    }

    @UiThread
    fun setAmount(amount: BigDecimal) {
        amountLiveData.value = amount
    }

    @UiThread
    fun changeCurrencyCode(currencyCode: String) {
        selectedCurrencyCode = currencyCode
        currencyListLiveData.value?.let { currencyList ->
            viewModelScope.launch(defaultDispatcher) {
                currencyRepository.updateAllOrdinals(currencyCode, currencyList)
            }
        }
    }

    private fun onCurrencyListChanged(amount: BigDecimal, list: List<Currency>) =
        viewModelScope.launch(defaultDispatcher) {
            _currencyItemUIModelList.postValue(
                currencyDisplayableItemMapper.toCurrencyItemUIModelList(list, amount)
            )
        }

    fun onClear() {
        currencyRepository.onClear()
    }
}
