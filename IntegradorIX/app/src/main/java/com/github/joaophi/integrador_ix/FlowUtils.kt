package com.github.joaophi.integrador_ix

import android.annotation.SuppressLint
import android.os.Looper
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.asFlow
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.awaitCancellation
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.*
import kotlin.properties.PropertyDelegateProvider
import kotlin.properties.ReadOnlyProperty

fun <T> SavedStateHandle.getStateFlow(initialValue: T) = PropertyDelegateProvider { _: Any?, prop ->
    val stateFlow: MutableStateFlow<T> = WrapperStateFlow(getLiveData(prop.name, initialValue))
    ReadOnlyProperty { _: Any?, _ -> stateFlow }
}

private class WrapperStateFlow<T>(private val liveData: MutableLiveData<T>) : MutableStateFlow<T> {
    override val subscriptionCount: StateFlow<Int>
        get() = TODO("Not yet implemented")

    override suspend fun emit(value: T) {
        this.value = value
    }

    @ExperimentalCoroutinesApi
    override fun resetReplayCache() {
        TODO("Not yet implemented")
    }

    override fun tryEmit(value: T): Boolean {
        this.value = value
        return true
    }

    override var value: T
        @Suppress("UNCHECKED_CAST")
        get() = liveData.value as T
        @SuppressLint("NullSafeMutableLiveData")
        set(value) = liveData.postValue(value)

    override fun compareAndSet(expect: T, update: T): Boolean {
        val equal = expect == value
        if (equal) value = update
        return equal
    }

    override val replayCache: List<T>
        get() = listOf(value)

    override suspend fun collect(collector: FlowCollector<T>): Nothing {
        liveData.asFlow().distinctUntilChanged().collect(collector)
        awaitCancellation()
    }
}

@SuppressLint("MissingPermission")
fun FusedLocationProviderClient.locationFlow(request: LocationRequest) = callbackFlow {
    val callback = object : LocationCallback() {
        override fun onLocationResult(result: LocationResult) {
            super.onLocationResult(result)
            result.locations.forEach(::trySend)
        }
    }
    requestLocationUpdates(request, callback, Looper.getMainLooper())
    awaitClose { removeLocationUpdates(callback) }
}