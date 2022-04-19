package com.github.joaophi.prova

import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import androidx.core.widget.doAfterTextChanged
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.coroutineScope
import androidx.lifecycle.flowWithLifecycle
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import kotlinx.coroutines.flow.*
import com.google.android.material.R as RM

fun TextInputEditText.bind(
    lifecycleOwner: LifecycleOwner,
    stateFlow: MutableStateFlow<String>,
    errorFlow: Flow<String?> = emptyFlow(),
) {
    val lifecycle = lifecycleOwner.lifecycle

    stateFlow
        .filter { it != text?.toString() }
        .onEach(::setText)
        .flowWithLifecycle(lifecycle)
        .launchIn(lifecycle.coroutineScope)
    setText(stateFlow.value)
    doAfterTextChanged { stateFlow.value = it?.toString().orEmpty() }

    val parent = parent.parent
    require(parent is TextInputLayout)
    errorFlow
        .onEach(parent::setError)
        .flowWithLifecycle(lifecycle)
        .launchIn(lifecycle.coroutineScope)
}

fun TextInputEditText.bindNumber(
    lifecycleOwner: LifecycleOwner,
    stateFlow: MutableStateFlow<Int?>,
    errorFlow: Flow<String?>,
) {
    val lifecycle = lifecycleOwner.lifecycle

    stateFlow
        .filter { it != text?.toString()?.toIntOrNull() }
        .map { it?.toString() }
        .onEach(::setText)
        .flowWithLifecycle(lifecycle)
        .launchIn(lifecycle.coroutineScope)
    setText(stateFlow.value?.toString())
    doAfterTextChanged { stateFlow.value = it?.toString()?.toIntOrNull() }

    val parent = parent.parent
    require(parent is TextInputLayout)
    errorFlow
        .onEach(parent::setError)
        .flowWithLifecycle(lifecycle)
        .launchIn(lifecycle.coroutineScope)
}

fun AutoCompleteTextView.bind(
    lifecycleOwner: LifecycleOwner,
    optionsFlow: Flow<List<Int>>,
    stateFlow: MutableStateFlow<Int?>,
    errorFlow: Flow<String?>,
) {
    val lifecycle = lifecycleOwner.lifecycle

    val adapter = ArrayAdapter<Int>(
        context,
        RM.layout.support_simple_spinner_dropdown_item,
    )
    setAdapter(adapter)

    optionsFlow
        .onEach {
            adapter.setNotifyOnChange(false)
            adapter.clear()
            adapter.addAll(it)
            adapter.notifyDataSetChanged()
        }
        .flowWithLifecycle(lifecycle)
        .launchIn(lifecycle.coroutineScope)

    stateFlow
        .onEach { setText(it?.toString(), false) }
        .flowWithLifecycle(lifecycle)
        .launchIn(lifecycle.coroutineScope)
    setText(stateFlow.value?.toString(), false)
    setOnItemClickListener { _, _, position, _ -> stateFlow.value = adapter.getItem(position) }

    val parent = parent.parent
    require(parent is TextInputLayout)
    errorFlow
        .onEach(parent::setError)
        .flowWithLifecycle(lifecycle)
        .launchIn(lifecycle.coroutineScope)
}