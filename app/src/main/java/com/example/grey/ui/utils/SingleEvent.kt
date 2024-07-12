package com.example.grey.ui.utils

import androidx.compose.runtime.Stable
import java.util.concurrent.atomic.AtomicBoolean

@Stable
class SingleEvent<T>(
    private val input: T
){
    val event: T?
        get() {
            return input.takeIf { isConsumed.getAndSet(true).not() }
        }
    private val isConsumed: AtomicBoolean = AtomicBoolean()
}

fun <T> T.asSingleEvent() = SingleEvent(this)
