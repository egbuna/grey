package com.example.grey.data.remote.response

import com.google.gson.annotations.SerializedName

data class SearchUser(
    @SerializedName("login")
    val login: String,
    @SerializedName("id")
    val id: String,
    @SerializedName("avatarUrl")
    val avatarUrl: String
)
