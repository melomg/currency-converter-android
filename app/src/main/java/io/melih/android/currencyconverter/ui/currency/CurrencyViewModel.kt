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

import androidx.annotation.VisibleForTesting
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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

    var amount: BigDecimal = DEFAULT_CURRENCY_AMOUNT

    @VisibleForTesting
    var selectedCurrencyCode = MutableLiveData<String>()

    private val _currencyListLiveData = MediatorLiveData<List<CurrencyItemUIModel>>()
    val currencyListLiveData: LiveData<List<CurrencyItemUIModel>>
        get() = _currencyListLiveData

    private val _errorLiveData = MutableLiveData<Event<Exception>>()
    val errorLiveData: LiveData<Event<Exception>>
        get() = _errorLiveData

    init {
        selectedCurrencyCode.value = DEFAULT_CURRENCY_CODE
        _currencyListLiveData.addSource(currencyRepository.getLatestCurrencyRateList()) { result ->
            when (result) {
                is Result.Error -> _errorLiveData.postValue(Event(result.exception))
                is Result.Success -> onSuccess(result.data)
            }
        }
        _currencyListLiveData.addSource(selectedCurrencyCode) {
            _currencyListLiveData.value?.apply { onSelectedCurrencyCodeChanged(this) }
        }
    }

    fun setSelectedCurrencyCode(currencyCode: String) {
        selectedCurrencyCode.value = currencyCode
    }

    private fun onSelectedCurrencyCodeChanged(data: List<CurrencyItemUIModel>) =
        viewModelScope.launch(defaultDispatcher) {
            selectedCurrencyCode.value?.let {
                _currencyListLiveData.postValue(currencyDisplayableItemMapper.modifyList(data, it))
            }
        }

    private fun onSuccess(data: List<Currency>) =
        viewModelScope.launch(defaultDispatcher) {
            selectedCurrencyCode.value?.let {
                _currencyListLiveData.postValue(
                    currencyDisplayableItemMapper.toCurrencyItemUIModelList(data, it, amount)
                )
            }
        }

    fun onClear() {
        currencyRepository.onClear()
    }
}
