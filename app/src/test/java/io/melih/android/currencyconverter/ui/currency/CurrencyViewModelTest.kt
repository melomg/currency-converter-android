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

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import io.melih.android.currencyconverter.MainCoroutineRule
import io.melih.android.currencyconverter.TestData.CURRENCY_ITEM_UI_MODEL_LIST
import io.melih.android.currencyconverter.TestData.CURRENCY_LIST
import io.melih.android.currencyconverter.getOrAwaitValue
import io.melih.android.currencyconverter.model.Currency
import io.melih.android.currencyconverter.model.Result
import io.melih.android.currencyconverter.observeForTesting
import io.melih.android.currencyconverter.repository.CurrencyRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.CoreMatchers.not
import org.hamcrest.CoreMatchers.nullValue
import org.junit.Assert
import org.junit.Assert.assertThat
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.Mockito.verify
import org.mockito.MockitoAnnotations

@ExperimentalCoroutinesApi
class CurrencyViewModelTest {

    private lateinit var currencyViewModel: CurrencyViewModel

    @Mock
    private lateinit var currencyRepository: CurrencyRepository

    @Mock
    private lateinit var currencyDisplayableItemMapper: CurrencyDisplayableItemMapper

    @ExperimentalCoroutinesApi
    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    private var currencyLiveData = MutableLiveData<Result<List<Currency>>>()

    @Before
    fun setupViewModel() {
        MockitoAnnotations.initMocks(this)
        `when`(currencyRepository.getLatestCurrencyRateList()).thenReturn(currencyLiveData)
        currencyLiveData.value = Result.Success(CURRENCY_LIST)
        currencyViewModel = CurrencyViewModel(
            currencyDisplayableItemMapper,
            mainCoroutineRule.testDispatcher,
            currencyRepository
        )
    }

    @Test
    fun `selectedCurrencyCode livedata value is not null when code is set`() {
        val currencyCode = currencyViewModel.selectedCurrencyCode.getOrAwaitValue()
        currencyViewModel.setSelectedCurrencyCode("EUR")

        assertThat(currencyCode, not(nullValue()))
    }

    @Test
    fun `currency code changes when it is set`() {
        currencyViewModel.setSelectedCurrencyCode("GBP")
        var currencyCode = currencyViewModel.selectedCurrencyCode.getOrAwaitValue()
        Assert.assertEquals(currencyCode, "GBP")

        currencyViewModel.setSelectedCurrencyCode("EUR")
        currencyCode = currencyViewModel.selectedCurrencyCode.getOrAwaitValue()
        Assert.assertEquals(currencyCode, "EUR")
    }

    @Test
    fun `setSelectedCurrencyCode triggers currencyListLiveData`() =
        mainCoroutineRule.testDispatcher.runBlockingTest {
            /* `when`(currencyDisplayableItemMapper.modifyList(emptyList(), "GBP")).thenReturn(
                 CURRENCY_ITEM_UI_MODEL_LIST
             )*/

            mainCoroutineRule.testDispatcher.pauseDispatcher()

            currencyViewModel.currencyItemUIModelList.observeForTesting {
                mainCoroutineRule.testDispatcher.resumeDispatcher()

                currencyViewModel.setSelectedCurrencyCode("GBP")

                val currencyList = currencyViewModel.currencyItemUIModelList.getOrAwaitValue()

                assertThat(currencyList, not(nullValue()))
                assertThat(currencyList, `is`(equalTo(CURRENCY_ITEM_UI_MODEL_LIST)))
            }
        }

    @Test
    fun `clears repository`() {
        currencyViewModel.onClear()
        verify(currencyRepository).onClear()
    }
}
