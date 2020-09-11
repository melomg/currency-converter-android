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
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.times
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import io.melih.android.currencyconverter.MainCoroutineRule
import io.melih.android.currencyconverter.TestData.CURRENCY_LIST
import io.melih.android.currencyconverter.core.model.Currency
import io.melih.android.currencyconverter.core.model.Result
import io.melih.android.currencyconverter.getOrAwaitValue
import io.melih.android.currencyconverter.repository.CurrencyRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.hamcrest.CoreMatchers.not
import org.hamcrest.CoreMatchers.nullValue
import org.junit.Assert
import org.junit.Assert.assertThat
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
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
        whenever(currencyRepository.getLatestCurrencyRateList()).thenReturn(currencyLiveData)
        currencyLiveData.value = Result.Success(CURRENCY_LIST)
        currencyViewModel = CurrencyViewModel(
            currencyDisplayableItemMapper,
            mainCoroutineRule.dispatcherProvider,
            currencyRepository
        )
    }

    @Test
    fun `selectedCurrencyCode livedata value is not null when code is set`() {
        // WHEN
        currencyViewModel.changeCurrencyCode("EUR")

        // THEN
        assertThat(currencyViewModel.selectedCurrencyCode, not(nullValue()))
    }

    @Test
    fun `currency code changes when it is set`() {
        // WHEN
        currencyViewModel.changeCurrencyCode("GBP")
        // THEN
        Assert.assertEquals(currencyViewModel.selectedCurrencyCode, "GBP")

        // WHEN
        currencyViewModel.changeCurrencyCode("EUR")
        // THEN
        Assert.assertEquals(currencyViewModel.selectedCurrencyCode, "EUR")
    }

    @Test
    fun `changeCurrencyCode triggers updateAllOrdinals`() = mainCoroutineRule.runBlockingTest {
        currencyViewModel.currencyItemUIModelList.getOrAwaitValue()

        // WHEN
        currencyViewModel.changeCurrencyCode("GBP")

        // THEN
        verify(currencyRepository, times(1)).updateAllOrdinals(any(), any())
    }

    @Test
    fun `clears repository`() {
        // WHEN
        currencyViewModel.onClear()

        // THEN
        verify(currencyRepository).onClear()
    }
}
