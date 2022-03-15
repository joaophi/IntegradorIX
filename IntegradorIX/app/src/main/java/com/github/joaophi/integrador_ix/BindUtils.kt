package com.github.joaophi.integrador_ix

import androidx.core.widget.doAfterTextChanged
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.coroutineScope
import androidx.lifecycle.flowWithLifecycle
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import kotlinx.coroutines.flow.*

fun TextInputEditText.bind(
    lifecycleOwner: LifecycleOwner,
    stateFlow: MutableStateFlow<String>,
    errorFlow: Flow<String?>,
) {
    val lifecycle = lifecycleOwner.lifecycle

    stateFlow
        .filter { it != text.toString() }
        .onEach(::setText)
        .flowWithLifecycle(lifecycle)
        .launchIn(lifecycle.coroutineScope)
    doAfterTextChanged { stateFlow.value = it.toString() }

    val parent = parent.parent
    require(parent is TextInputLayout)
    errorFlow
        .onEach(parent::setError)
        .flowWithLifecycle(lifecycle)
        .launchIn(lifecycle.coroutineScope)
}