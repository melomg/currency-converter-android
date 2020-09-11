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

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import dagger.hilt.android.AndroidEntryPoint
import io.melih.android.currencyconverter.R
import io.melih.android.currencyconverter.core.exceptions.CurrenciesNotFound
import io.melih.android.currencyconverter.ui.currency.adapter.CurrencyRateListRecyclerAdapter
import io.melih.android.currencyconverter.util.event.EventObserver
import io.melih.android.currencyconverter.util.hideKeyboard
import kotlinx.android.synthetic.main.activity_currency_rates.*
import timber.log.Timber

@AndroidEntryPoint
class CurrencyRatesActivity : AppCompatActivity() {

    private val viewModel: CurrencyViewModel by viewModels()

    private lateinit var currencyAdapter: CurrencyRateListRecyclerAdapter

    private val adapterDataObserver = object : RecyclerView.AdapterDataObserver() {
        override fun onItemRangeMoved(fromPosition: Int, toPosition: Int, itemCount: Int) {
            super.onItemRangeMoved(fromPosition, toPosition, itemCount)
            recyclerView.scrollToPosition(0)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_currency_rates)
        setSupportActionBar(toolbar)

        setupRecyclerView()

        viewModel.currencyItemUIModelList.observe(this, Observer {
            currencyAdapter.submitList(it ?: return@Observer)
        })

        viewModel.errorLiveData.observe(this, EventObserver { exception ->
            handleError(exception)
        })
    }

    override fun onDestroy() {
        super.onDestroy()
        viewModel.onClear()
        currencyAdapter.unregisterAdapterDataObserver(adapterDataObserver)
    }

    private fun setupRecyclerView() {
        currencyAdapter = CurrencyRateListRecyclerAdapter(CurrencyDiffCallback, { amount ->
            viewModel.setAmount(amount)
        }, { currencyItemUIModel ->
            viewModel.changeCurrencyCode(currencyItemUIModel.currencyCode)
        })

        recyclerView.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(context)
            adapter = currencyAdapter

            setOnTouchListener { _, _ ->
                hideKeyboard()
                return@setOnTouchListener false
            }
        }

        currencyAdapter.registerAdapterDataObserver(adapterDataObserver)
    }

    private fun handleError(exception: Exception) {
        when (exception) {
            is CurrenciesNotFound -> Timber.e(exception)
        }
    }
}
