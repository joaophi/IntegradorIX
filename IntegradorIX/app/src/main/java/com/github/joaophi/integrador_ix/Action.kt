package com.github.joaophi.integrador_ix

import android.os.Parcelable
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch
import kotlinx.parcelize.Parcelize

sealed class Action : Parcelable {
    @Parcelize
    object Running : Action()

    @Parcelize
    data class Error(val throwable: Throwable) : Action()

    @Parcelize
    object Done : Action()
}

fun CoroutineScope.launchAction(action: MutableSharedFlow<Action>, block: suspend () -> Unit) {
    launch {
        action.emit(Action.Running)
        try {
            block()
            action.emit(Action.Done)
        } catch (ex: Throwable) {
            action.emit(Action.Error(ex))
        }
    }
}