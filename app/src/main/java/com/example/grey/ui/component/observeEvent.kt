package com.example.grey.ui.component

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import com.example.grey.ui.utils.SingleEvent

@Composable
fun <T> observeEvent(
    event: SingleEvent<T>?,
    action: suspend (T) -> Unit
) {
    LaunchedEffect(key1 = event) {
        event?.event?.let {
            action(it)
        }
    }
}
