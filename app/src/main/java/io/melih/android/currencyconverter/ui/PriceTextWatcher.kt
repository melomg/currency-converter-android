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
package io.melih.android.currencyconverter.ui

import android.text.Editable
import android.text.InputFilter
import android.text.TextWatcher
import android.widget.EditText
import io.melih.android.currencyconverter.util.formatCurrencyAmount
import kotlin.math.min

class PriceTextWatcher(
    private val editText: EditText
) : TextWatcher {

    private var current: String? = null

    override fun afterTextChanged(s: Editable?) {
        // no-op
    }

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
        // no-op
    }

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        val text = s.toString()
        if (text != current) {
            editText.removeTextChangedListener(this)

            current = formatCurrencyAmount(text)

            val cursorPositionFromEnd = text.length - editText.selectionStart
            if (current == ZERO) {
                editText.text = null
                current = null
            } else {
                editText.setText(current)
            }

            current?.let {
                setSelection(it, cursorPositionFromEnd)
            }

            editText.addTextChangedListener(this)
        }
    }

    private fun getMaxLengthFromEditText(): Int {
        for (filter: InputFilter in editText.filters) {
            if (filter is InputFilter.LengthFilter) {
                return filter.max
            }
        }
        return 0
    }

    private fun setSelection(currentText: String, cursorPositionFromEnd: Int) {
        var selection = currentText.length - cursorPositionFromEnd

        val maxValidLengthOfSelection = min(currentText.length, getMaxLengthFromEditText())
        if (selection >= maxValidLengthOfSelection) {
            selection = maxValidLengthOfSelection - 1
        }
        if (selection < 0) {
            selection = 0
        }
        editText.setSelection(selection)
    }

    companion object {
        private const val ZERO: String = "0"
    }
}
