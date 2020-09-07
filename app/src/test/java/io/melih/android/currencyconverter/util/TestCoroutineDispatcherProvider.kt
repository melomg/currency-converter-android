package io.melih.android.currencyconverter.util

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestCoroutineDispatcher

@ExperimentalCoroutinesApi
class TestCoroutineDispatcherProvider : CoroutineDispatcherProvider() {
    val testDispatcher = TestCoroutineDispatcher()

    override val main: CoroutineDispatcher = testDispatcher

    override val io: CoroutineDispatcher = testDispatcher

    override val default: CoroutineDispatcher = testDispatcher
}
