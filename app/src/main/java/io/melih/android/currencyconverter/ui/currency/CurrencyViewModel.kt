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
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.melih.android.currencyconverter.core.CoroutineDispatcherProvider
import io.melih.android.currencyconverter.core.model.Currency
import io.melih.android.currencyconverter.core.model.Result
import io.melih.android.currencyconverter.repository.CurrencyRepository
import io.melih.android.currencyconverter.util.event.Event
import java.math.BigDecimal
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class CurrencyViewModel @ViewModelInject constructor(
    private val currencyRepository: CurrencyRepository,
    private val currencyDisplayableItemMapper: CurrencyDisplayableItemMapper,
    private val dispatcherProvider: CoroutineDispatcherProvider
) : ViewModel() {

    private val _currencyItemUIModelList = MediatorLiveData<List<CurrencyItemUIModel>>()
    private val _errorLiveData = MutableLiveData<Event<Exception>>()
    private val amountLiveData = MutableLiveData<BigDecimal>()
    private val currencyListLiveData = MediatorLiveData<List<Currency>>()
    @VisibleForTesting
    var selectedCurrencyCode: String? = null

    val currencyItemUIModelList: LiveData<List<CurrencyItemUIModel>> by this::_currencyItemUIModelList
    val errorLiveData: LiveData<Event<Exception>> by this::_errorLiveData

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

        currencyListLiveData.addSource(amountLiveData) { amount ->
            val currencyCode = selectedCurrencyCode
            val currencyList = currencyListLiveData.value
            if (currencyCode != null && currencyList != null) {
                onCurrencyListChanged(amount, currencyList)
            }
        }

        viewModelScope.launch(dispatcherProvider.io) {
            currencyRepository.getLatestCurrencyRateList().collect { result ->
                when (result) {
                    is Result.Error -> _errorLiveData.postValue(Event(result.exception))
                    is Result.Success -> currencyListLiveData.postValue(result.data)
                }
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
            viewModelScope.launch(dispatcherProvider.io) {
                currencyRepository.updateAllOrdinals(currencyCode, currencyList)
            }
        }
    }

    private fun onCurrencyListChanged(amount: BigDecimal, list: List<Currency>) =
        viewModelScope.launch(dispatcherProvider.io) {
            _currencyItemUIModelList.postValue(
                currencyDisplayableItemMapper.toCurrencyItemUIModelList(list, amount)
            )
        }

    fun onClear() {
        currencyRepository.onClear()
    }
}
