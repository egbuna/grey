package com.example.grey.ui.user.detail

import com.example.grey.data.remote.response.UserDetailResponse
import com.example.grey.ui.utils.SingleEvent

data class UserDetailState(
    val userDetailResponse: UserDetailResponse? = null,
    val isLoading: Boolean = false,
    val errorMessage: SingleEvent<String>? = null
)
