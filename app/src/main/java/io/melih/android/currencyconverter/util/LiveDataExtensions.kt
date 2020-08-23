package io.melih.android.currencyconverter.util

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData

fun <E> MediatorLiveData<out Any>.addSourceSafely(source: LiveData<E>, onChanged: (result: E) -> Unit) {
    removeSource(source)
    addSource(source, onChanged)
}
