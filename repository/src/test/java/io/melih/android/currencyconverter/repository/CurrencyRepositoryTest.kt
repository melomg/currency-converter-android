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
package io.melih.android.currencyconverter.repository

import com.nhaarman.mockitokotlin2.whenever
import io.melih.android.currencyconverter.core.exceptions.CurrenciesNotFound
import io.melih.android.currencyconverter.core.model.Currency
import io.melih.android.currencyconverter.core.model.Result
import io.melih.android.currencyconverter.core.moveSelectedCurrencyToTop
import io.melih.android.currencyconverter.remotedatasource.CurrencyRemoteDataSource
import io.melih.android.currencyconverter.unittestshared.MainCoroutineRule
import io.melih.android.currencyconverter.unittestshared.UPDATED_CURRENCY_LIST
import io.melih.android.currencyconverter.unittestshared.getCurrencyListTestData
import io.melih.android.currencyconverter.unittestshared.test
import kotlin.test.assertEquals
import kotlin.test.assertTrue
import kotlin.time.ExperimentalTime
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.runBlockingTest
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.mockito.Mock
import org.mockito.MockitoAnnotations

@ExperimentalCoroutinesApi
@RunWith(JUnit4::class)
class CurrencyRepositoryTest {
    private lateinit var repository: CurrencyRepository

    private var local = FakeInMemoryCurrencyLocalDatasource()
    @Mock
    private lateinit var remote: CurrencyRemoteDataSource

    @get:Rule
    var coroutineScope = MainCoroutineRule()

    @Before
    fun setup() {
        MockitoAnnotations.initMocks(this)
        local.clearAndAddCurrencies(getCurrencyListTestData())
    }

    @After
    fun tearDown() {
        local.clearCurrencies()
    }

    @ExperimentalTime
    @Test
    fun `local data is preserved when remote returns error`() = coroutineScope.runBlockingTest {
        // GIVEN
        whenever(remote.getLatestCurrencyRateList()).thenReturn(Result.Error(CurrenciesNotFound))

        // WHEN
        repository = CurrencyRepository(local, remote, coroutineScope.dispatcherProvider)

        advanceTimeBy(1000L)
        // THEN
        repository.getLatestCurrencyRateList().test {
            val firstResult = expectItem()
            assertTrue(firstResult is Result.Success)
            assertEquals(expected = local.getAll(), actual = firstResult.data)
            expectComplete()
        }
    }

    @ExperimentalTime
    @Test
    fun `local data is updated when remote returns success`() = coroutineScope.runBlockingTest {
        // GIVEN
        whenever(remote.getLatestCurrencyRateList()).thenReturn(Result.Success(UPDATED_CURRENCY_LIST))

        // WHEN
        repository = CurrencyRepository(local, remote, coroutineScope.dispatcherProvider)

        // THEN
        repository.getLatestCurrencyRateList().test {
            val firstResult = expectItem()
            assertTrue(firstResult is Result.Success)
            assertEquals(expected = UPDATED_CURRENCY_LIST, actual = firstResult.data)

            expectComplete()
        }
    }

    @ExperimentalTime
    @Test
    fun `updateAllOrdinals moves selected currency to top`() = coroutineScope.runBlockingTest {
        // GIVEN
        whenever(remote.getLatestCurrencyRateList()).thenReturn(Result.Error(CurrenciesNotFound))
        repository = CurrencyRepository(local, remote, coroutineScope.dispatcherProvider)
        val selectedCurrencyCode = "CAD"
        val expectedCurrencyList = local.getAll().changeOrdinalsAndMoveSelectedCurrencyToTop(selectedCurrencyCode)

        // WHEN
        launch { repository.updateAllOrdinals(selectedCurrencyCode) }
        advanceTimeBy(1000L)

        // THEN
        repository.getLatestCurrencyRateList().test {
            val firstResult = expectItem()
            assertTrue(firstResult is Result.Success)
            assertEquals(expected = expectedCurrencyList, actual = firstResult.data)

            expectComplete()
        }
    }

    private fun List<Currency>.changeOrdinalsAndMoveSelectedCurrencyToTop(@Suppress("SameParameterValue") selectedCurrencyCode: String): List<Currency> {
        val currencyList = mutableListOf<Currency>()
        forEach { currencyList.add(it.copy()) }

        moveSelectedCurrencyToTop(currencyList) { it.currencyCode == selectedCurrencyCode }

        currencyList.forEachIndexed { index, currency ->
            currency.ordinal = index
        }

        return currencyList
    }
}
